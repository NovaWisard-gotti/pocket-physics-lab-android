package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Vista combinada de una sesión de experimento con toda su información
 * (predicción, resumen de medición y resultado) para mostrarla en el
 * Registro científico sin hacer varias consultas manuales.
 */
data class ExperimentSessionFull(
    @Embedded
    val session: ExperimentSession,

    @Relation(parentColumn = "experimentKey", entityColumn = "experimentKey")
    val experiment: SensorExperiment?,

    @Relation(parentColumn = "id", entityColumn = "sessionId")
    val prediction: Prediction?,

    @Relation(parentColumn = "id", entityColumn = "sessionId")
    val summary: MeasurementSummary?,

    @Relation(parentColumn = "id", entityColumn = "sessionId")
    val result: ExperimentResult?
)
