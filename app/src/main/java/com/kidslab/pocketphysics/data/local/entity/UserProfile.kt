package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Perfil del pequeño científico o científica que usa la app.
 * Se crea en la pantalla "Científico de bolsillo".
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatarKey: String,
    val createdAt: Long
)
