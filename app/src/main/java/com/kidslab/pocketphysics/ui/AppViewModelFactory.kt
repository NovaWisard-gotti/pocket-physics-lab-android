package com.kidslab.pocketphysics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.kidslab.pocketphysics.di.AppContainer
import com.kidslab.pocketphysics.ui.screens.challenges.ChallengesViewModel
import com.kidslab.pocketphysics.ui.screens.experiments.ExperimentsViewModel
import com.kidslab.pocketphysics.ui.screens.light.LightLabViewModel
import com.kidslab.pocketphysics.ui.screens.log.LogViewModel
import com.kidslab.pocketphysics.ui.screens.motion.MotionLabViewModel
import com.kidslab.pocketphysics.ui.screens.profile.ProfileSelectorViewModel
import com.kidslab.pocketphysics.ui.screens.profile.ProfileViewModel
import com.kidslab.pocketphysics.ui.screens.rotation.RotationLabViewModel
import com.kidslab.pocketphysics.ui.screens.sensors.SensorsViewModel
import com.kidslab.pocketphysics.ui.screens.sound.SoundLabViewModel

/**
 * Fábrica manual de ViewModels: no se usa Hilt/Dagger. Cada ViewModel recibe
 * sus dependencias directamente desde [AppContainer].
 */
class AppViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when (modelClass) {
            ProfileViewModel::class.java -> ProfileViewModel(container.profileRepository) as T
            ProfileSelectorViewModel::class.java -> ProfileSelectorViewModel(container.profileRepository) as T
            SensorsViewModel::class.java -> SensorsViewModel(container.sensorRepository, container.soundRepository) as T
            MotionLabViewModel::class.java -> MotionLabViewModel(
                container.sensorRepository, container.profileRepository, container.experimentRepository, container.challengeRepository
            ) as T
            RotationLabViewModel::class.java -> RotationLabViewModel(
                container.sensorRepository, container.profileRepository, container.experimentRepository, container.challengeRepository
            ) as T
            SoundLabViewModel::class.java -> SoundLabViewModel(
                container.soundRepository, container.profileRepository, container.experimentRepository, container.challengeRepository
            ) as T
            LightLabViewModel::class.java -> LightLabViewModel(
                container.sensorRepository, container.profileRepository, container.experimentRepository, container.challengeRepository
            ) as T
            ExperimentsViewModel::class.java -> ExperimentsViewModel(container.experimentRepository) as T
            LogViewModel::class.java -> LogViewModel(container.profileRepository, container.experimentRepository) as T
            ChallengesViewModel::class.java -> ChallengesViewModel(container.profileRepository, container.challengeRepository) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
