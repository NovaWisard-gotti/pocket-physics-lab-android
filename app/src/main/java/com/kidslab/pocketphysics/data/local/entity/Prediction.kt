package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * La predicción que el niño o niña escribe/elige ANTES de medir.
 * Es el paso "Predicción" del método científico simplificado usado en toda la app.
 */
@Entity(
    tableName = "prediction",
    foreignKeys = [
        ForeignKey(
            entity = ExperimentSession::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId", unique = true)]
)
data class Prediction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val textoPrediccion: String
)
