package com.kidslab.pocketphysics.di

import android.content.Context
import com.kidslab.pocketphysics.data.audio.AudioRecordSoundRepository
import com.kidslab.pocketphysics.data.local.AppDatabase
import com.kidslab.pocketphysics.data.repository.ChallengeRepository
import com.kidslab.pocketphysics.data.repository.ExperimentRepository
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import com.kidslab.pocketphysics.data.repository.SensorRepository
import com.kidslab.pocketphysics.data.repository.SoundRepository
import com.kidslab.pocketphysics.data.sensors.SensorManagerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers

/**
 * Contenedor de inyección de dependencias manual (sin Hilt/Dagger), tal
 * como se hizo en HabitHero y Money Explorer. Expone la base de datos y
 * los repositorios a toda la app, construidos una única vez.
 */
class AppContainer(context: Context) {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: AppDatabase = AppDatabase.getInstance(context, applicationScope)

    val sensorRepository: SensorRepository = SensorManagerRepository(context)
    val soundRepository: SoundRepository = AudioRecordSoundRepository(context)

    val profileRepository = ProfileRepository(database.userProfileDao())
    val experimentRepository = ExperimentRepository(database.sensorExperimentDao(), database.experimentSessionDao())
    val challengeRepository = ChallengeRepository(
        database.challengeDao(),
        database.challengeCompletionDao(),
        database.badgeDao(),
        database.userBadgeDao()
    )
}
