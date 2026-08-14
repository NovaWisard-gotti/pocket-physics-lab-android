package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Resultado y conclusión de un experimento: si la predicción se cumplió
 * y una frase corta de conclusión, generada a partir del resumen de medición.
 */
@Entity(
    tableName = "experiment_result",
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
data class ExperimentResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val prediccionCorrecta: Boolean,
    val textoResultado: String,
    val textoConclusion: String
)
