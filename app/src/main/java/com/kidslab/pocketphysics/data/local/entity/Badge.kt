package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Catálogo de insignias que se pueden ganar completando desafíos.
 */
@Entity(tableName = "badge")
data class Badge(
    @PrimaryKey
    val badgeKey: String,
    val titulo: String,
    val descripcion: String,
    val iconKey: String
)
