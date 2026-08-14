package com.kidslab.pocketphysics.data.repository

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kidslab.pocketphysics.data.local.AppDatabase
import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Pruebas del sistema de desafíos e insignias: una insignia solo se debe
 * otorgar la primera vez que se cumple su desafío asociado.
 */
@RunWith(RobolectricTestRunner::class)
class ChallengeRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: ChallengeRepository
    private var profileId: Long = 0

    @Before
    fun setUp() = runTest {
        db = AppDatabase.buildInMemory(ApplicationProvider.getApplicationContext())
        repository = ChallengeRepository(db.challengeDao(), db.challengeCompletionDao(), db.badgeDao(), db.userBadgeDao())
        profileId = db.userProfileDao().insert(UserProfile(name = "Ada", avatarKey = "atomo", createdAt = 0L))
        db.challengeDao().insertAll(listOf(Challenge("desafio_mas_quieto", "Título", "Descripción", "ACCELEROMETER", "badge_explorador_quieto")))
        db.badgeDao().insertAll(listOf(Badge("badge_explorador_quieto", "Explorador", "Descripción", "leaf")))
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun `completar un desafio por primera vez otorga la insignia`() = runTest {
        val challenge = Challenge("desafio_mas_quieto", "Título", "Descripción", "ACCELEROMETER", "badge_explorador_quieto")
        val insigniaNueva = repository.completeChallenge(profileId, challenge, sessionId = 1L)
        assertThat(insigniaNueva).isTrue()
    }

    @Test
    fun `completar el mismo desafio dos veces no otorga la insignia dos veces`() = runTest {
        val challenge = Challenge("desafio_mas_quieto", "Título", "Descripción", "ACCELEROMETER", "badge_explorador_quieto")
        repository.completeChallenge(profileId, challenge, sessionId = 1L)
        val segundaVez = repository.completeChallenge(profileId, challenge, sessionId = 2L)
        assertThat(segundaVez).isFalse()
    }

    @Test
    fun `grantBadgeIfNew solo devuelve true la primera vez`() = runTest {
        val primeraVez = repository.grantBadgeIfNew(profileId, "badge_explorador_quieto")
        val segundaVez = repository.grantBadgeIfNew(profileId, "badge_explorador_quieto")
        assertThat(primeraVez).isTrue()
        assertThat(segundaVez).isFalse()
    }
}
