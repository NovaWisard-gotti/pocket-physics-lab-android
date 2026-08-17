package com.kidslab.pocketphysics.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kidslab.pocketphysics.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

val AVATARES_DISPONIBLES = listOf("atomo", "cohete", "lupa", "iman", "prisma", "engranaje")

data class ProfileUiState(
    val nombreEnEdicion: String = "",
    val avatarSeleccionado: String = AVATARES_DISPONIBLES.first(),
    val guardando: Boolean = false,
    val perfilCreado: Boolean = false
)

/** Crea un perfil NUEVO que empieza desde cero (sin progreso previo). */
class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onNombreChanged(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreEnEdicion = nombre)
    }

    fun onAvatarSeleccionado(avatarKey: String) {
        _uiState.value = _uiState.value.copy(avatarSeleccionado = avatarKey)
    }

    fun guardarPerfil() {
        val nombre = _uiState.value.nombreEnEdicion.trim()
        if (nombre.isEmpty() || _uiState.value.guardando) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(guardando = true)
            repository.createProfile(nombre, _uiState.value.avatarSeleccionado)
            _uiState.value = _uiState.value.copy(guardando = false, perfilCreado = true)
        }
    }
}
