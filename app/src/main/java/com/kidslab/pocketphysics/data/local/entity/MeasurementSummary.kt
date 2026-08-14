package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Resumen estadístico de una medición de sensor: mínimo, máximo, promedio
 * y duración. NUNCA se guardan las muestras individuales (que pueden llegar
 * a cientos por segundo); solo este resumen, para no inflar la base de datos.
 */
@Entity(
    tableName = "measurement_summary",
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
data class MeasurementSummary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val sensorType: String,
    val valorMinimo: Float,
    val valorMaximo: Float,
    val valorPromedio: Float,
    val duracionMs: Long,
    val muestrasAnalizadas: Int
)
