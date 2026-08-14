package com.kidslab.pocketphysics.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

val AVATARES_DISPONIBLES = listOf("atomo", "cohete", "lupa", "iman", "prisma", "engranaje")

data class ProfileUiState(
    val profile: UserProfile? = null,
    val nombreEnEdicion: String = "",
    val avatarSeleccionado: String = AVATARES_DISPONIBLES.first(),
    val guardando: Boolean = false,
    val perfilCreado: Boolean = false
)

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeProfile().collect { profile ->
                _uiState.value = _uiState.value.copy(
                    profile = profile,
                    nombreEnEdicion = profile?.name ?: _uiState.value.nombreEnEdicion,
                    avatarSeleccionado = profile?.avatarKey ?: _uiState.value.avatarSeleccionado,
                    perfilCreado = profile != null
                )
            }
        }
    }

    fun onNombreChanged(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreEnEdicion = nombre)
    }

    fun onAvatarSeleccionado(avatarKey: String) {
        _uiState.value = _uiState.value.copy(avatarSeleccionado = avatarKey)
    }

    fun guardarPerfil() {
        val nombre = _uiState.value.nombreEnEdicion.trim()
        if (nombre.isEmpty()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(guardando = true)
            repository.createOrUpdateProfile(nombre, _uiState.value.avatarSeleccionado)
            _uiState.value = _uiState.value.copy(guardando = false, perfilCreado = true)
        }
    }
}
