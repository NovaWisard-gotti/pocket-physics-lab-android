package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Una sesión concreta en la que el niño o niña realizó un experimento.
 * Es el "registro científico" central: une la predicción, el resumen de
 * mediciones y el resultado de una ejecución particular.
 *
 * IMPORTANTE: no se guardan miles de muestras por segundo aquí, solo
 * referencias a resúmenes ya calculados (ver [MeasurementSummary]).
 */
@Entity(
    tableName = "experiment_session",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SensorExperiment::class,
            parentColumns = ["experimentKey"],
            childColumns = ["experimentKey"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("experimentKey")]
)
data class ExperimentSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val experimentKey: String,
    val fecha: Long,
    val durationMs: Long
)
