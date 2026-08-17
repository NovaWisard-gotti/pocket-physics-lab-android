package com.kidslab.pocketphysics.data.repository

import com.kidslab.pocketphysics.data.local.ActiveProfileStore
import com.kidslab.pocketphysics.data.local.dao.UserProfileDao
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Repositorio de perfiles del pequeño científico (pantalla "Científico de
 * bolsillo"). La app admite varios perfiles locales en el mismo teléfono:
 * cada uno guarda su propio progreso, y [activeProfileStore] recuerda cuál
 * fue el último elegido para poder continuar donde se dejó.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileRepository(
    private val dao: UserProfileDao,
    private val activeProfileStore: ActiveProfileStore
) {

    /** Lista de todos los perfiles guardados, para la pantalla de selección. */
    fun observeProfiles(): Flow<List<UserProfile>> = dao.observeAll()

    /** El perfil actualmente activo (o null si nadie ha elegido ninguno todavía). */
    fun observeProfile(): Flow<UserProfile?> =
        activeProfileStore.activeProfileId.flatMapLatest { id ->
            if (id == null) flowOf(null) else dao.observeById(id)
        }

    /** Crea un perfil nuevo que empieza desde cero, y lo deja como activo. */
    suspend fun createProfile(name: String, avatarKey: String): Long {
        val id = dao.insert(UserProfile(name = name, avatarKey = avatarKey, createdAt = System.currentTimeMillis()))
        activeProfileStore.setActiveProfile(id)
        return id
    }

    /** Elige un perfil ya existente para continuar donde se dejó. */
    fun selectProfile(profileId: Long) {
        activeProfileStore.setActiveProfile(profileId)
    }

    /** Vuelve a la pantalla de selección de perfiles. */
    fun clearActiveProfile() {
        activeProfileStore.clearActiveProfile()
    }
}
