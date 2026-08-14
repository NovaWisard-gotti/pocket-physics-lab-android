package com.kidslab.pocketphysics.ui.screens.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.local.entity.ExperimentSessionFull
import com.kidslab.pocketphysics.data.repository.ExperimentRepository
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class LogUiState(
    val sesiones: List<ExperimentSessionFull> = emptyList(),
    val cargando: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class LogViewModel(
    private val profileRepository: ProfileRepository,
    private val experimentRepository: ExperimentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    init {
        profileRepository.observeProfile()
            .flatMapLatest { profile ->
                if (profile == null) {
                    kotlinx.coroutines.flow.flowOf(emptyList())
                } else {
                    experimentRepository.observeSessionsForProfile(profile.id)
                }
            }
            .onEach { sesiones -> _uiState.value = LogUiState(sesiones = sesiones, cargando = false) }
            .launchIn(viewModelScope)
    }
}
