package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Catálogo de los experimentos guiados disponibles en la app (mínimo 15).
 * Cada fila describe UN experimento: qué sensor usa, la pregunta que
 * plantea al niño o niña, y a qué laboratorio pertenece.
 *
 * Esta tabla se llena una sola vez al crear la base de datos (seed),
 * y las [ExperimentSession] hacen referencia a estas filas mediante [experimentKey].
 */
@Entity(tableName = "sensor_experiment")
data class SensorExperiment(
    @PrimaryKey
    val experimentKey: String,
    val sensorType: String,       // "ACCELEROMETER" | "GYROSCOPE" | "MICROPHONE" | "LIGHT"
    val labKey: String,           // "movimiento" | "rotacion" | "sonido" | "luz"
    val titulo: String,
    val pregunta: String,
    val ordenSugerido: Int
)
