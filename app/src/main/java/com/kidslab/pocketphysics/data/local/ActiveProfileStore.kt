package com.kidslab.pocketphysics.data.local

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Recuerda qué perfil fue el último elegido en la pantalla de selección,
 * para que al reabrir la app se pueda continuar justo donde se dejó (o
 * mostrar la lista de perfiles si nadie ha elegido ninguno todavía).
 *
 * Se guarda fuera de Room porque es un simple "puntero", no un dato
 * científico: usar SharedPreferences es suficiente y evita migraciones.
 */
class ActiveProfileStore(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _activeProfileId = MutableStateFlow(readStoredId())
    val activeProfileId: StateFlow<Long?> = _activeProfileId

    fun setActiveProfile(profileId: Long) {
        prefs.edit().putLong(KEY_ACTIVE_PROFILE_ID, profileId).apply()
        _activeProfileId.value = profileId
    }

    /** Olvida el perfil activo, para volver a la pantalla de selección de perfiles. */
    fun clearActiveProfile() {
        prefs.edit().remove(KEY_ACTIVE_PROFILE_ID).apply()
        _activeProfileId.value = null
    }

    private fun readStoredId(): Long? {
        val value = prefs.getLong(KEY_ACTIVE_PROFILE_ID, -1L)
        return if (value == -1L) null else value
    }

    private companion object {
        const val PREFS_NAME = "pocket_physics_active_profile"
        const val KEY_ACTIVE_PROFILE_ID = "active_profile_id"
    }
}
