package com.kidslab.pocketphysics.data.repository

import com.kidslab.pocketphysics.data.local.dao.UserProfileDao
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

/** Repositorio del perfil del pequeño científico (pantalla "Científico de bolsillo"). */
class ProfileRepository(private val dao: UserProfileDao) {

    fun observeProfile(): Flow<UserProfile?> = dao.observeFirstProfile()

    suspend fun createOrUpdateProfile(name: String, avatarKey: String): Long {
        val existingCount = dao.count()
        return if (existingCount == 0) {
            dao.insert(UserProfile(name = name, avatarKey = avatarKey, createdAt = System.currentTimeMillis()))
        } else {
            // La app es de un solo perfil local: se actualiza el primero existente.
            val existingId = dao.getById(1)?.id ?: 1L
            dao.update(UserProfile(id = existingId, name = name, avatarKey = avatarKey, createdAt = System.currentTimeMillis()))
            existingId
        }
    }
}
