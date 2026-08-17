package com.kidslab.pocketphysics.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileSelectorUiState(
    val perfiles: List<UserProfile> = emptyList(),
    val cargando: Boolean = true,
    val perfilElegido: Boolean = false
)

/** Pantalla inicial: elegir un perfil ya guardado para continuar, o crear uno nuevo. */
class ProfileSelectorViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSelectorUiState())
    val uiState: StateFlow<ProfileSelectorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeProfiles().collect { perfiles ->
                _uiState.value = _uiState.value.copy(perfiles = perfiles, cargando = false)
            }
        }
    }

    fun elegirPerfil(profileId: Long) {
        repository.selectProfile(profileId)
        _uiState.value = _uiState.value.copy(perfilElegido = true)
    }
}
