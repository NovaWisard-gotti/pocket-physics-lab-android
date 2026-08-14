package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Catálogo de desafíos (ej. "Encuentra el momento más quieto",
 * "Gira 90° aproximadamente"). Tabla de referencia, sembrada una vez.
 */
@Entity(tableName = "challenge")
data class Challenge(
    @PrimaryKey
    val challengeKey: String,
    val titulo: String,
    val descripcion: String,
    val sensorType: String,
    val badgeKey: String
)
