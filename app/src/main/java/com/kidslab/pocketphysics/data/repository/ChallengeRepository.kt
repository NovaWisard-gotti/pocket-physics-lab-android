package com.kidslab.pocketphysics.data.repository

import com.kidslab.pocketphysics.data.local.dao.BadgeDao
import com.kidslab.pocketphysics.data.local.dao.ChallengeCompletionDao
import com.kidslab.pocketphysics.data.local.dao.ChallengeDao
import com.kidslab.pocketphysics.data.local.dao.UserBadgeDao
import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.data.local.entity.ChallengeCompletion
import com.kidslab.pocketphysics.data.local.entity.UserBadge
import kotlinx.coroutines.flow.Flow

/** Repositorio de desafíos e insignias. */
class ChallengeRepository(
    private val challengeDao: ChallengeDao,
    private val completionDao: ChallengeCompletionDao,
    private val badgeDao: BadgeDao,
    private val userBadgeDao: UserBadgeDao
) {
    fun observeChallenges(): Flow<List<Challenge>> = challengeDao.observeAll()

    fun observeCompletions(profileId: Long): Flow<List<ChallengeCompletion>> =
        completionDao.observeForProfile(profileId)

    fun observeBadges(): Flow<List<Badge>> = badgeDao.observeAll()

    fun observeUserBadges(profileId: Long): Flow<List<UserBadge>> = userBadgeDao.observeForProfile(profileId)

    /**
     * Marca un desafío como completado (si no lo estaba ya) y otorga su
     * insignia asociada la primera vez. Devuelve true si se otorgó una
     * insignia nueva (para poder celebrarlo en la interfaz).
     */
    suspend fun completeChallenge(profileId: Long, challenge: Challenge, sessionId: Long?): Boolean {
        val yaCompletado = completionDao.countCompletion(profileId, challenge.challengeKey) > 0
        if (!yaCompletado) {
            completionDao.insert(
                ChallengeCompletion(
                    profileId = profileId,
                    challengeKey = challenge.challengeKey,
                    sessionId = sessionId,
                    completedAt = System.currentTimeMillis()
                )
            )
        }
        val yaTieneInsignia = userBadgeDao.countBadge(profileId, challenge.badgeKey) > 0
        if (!yaTieneInsignia) {
            userBadgeDao.insert(
                UserBadge(profileId = profileId, badgeKey = challenge.badgeKey, earnedAt = System.currentTimeMillis())
            )
            return true
        }
        return false
    }

    suspend fun grantBadgeIfNew(profileId: Long, badgeKey: String): Boolean {
        val yaTiene = userBadgeDao.countBadge(profileId, badgeKey) > 0
        if (!yaTiene) {
            userBadgeDao.insert(UserBadge(profileId = profileId, badgeKey = badgeKey, earnedAt = System.currentTimeMillis()))
            return true
        }
        return false
    }
}
