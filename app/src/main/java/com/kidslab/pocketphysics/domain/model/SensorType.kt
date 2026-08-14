package com.kidslab.pocketphysics.domain.model

/**
 * Los cuatro sensores que explora Física de Bolsillo.
 * LIGHT es opcional: no todos los dispositivos lo tienen.
 */
enum class SensorType {
    ACCELEROMETER,
    GYROSCOPE,
    MICROPHONE,
    LIGHT
}

/** Disponibilidad de un sensor en el dispositivo actual, con una frase explicativa. */
data class SensorAvailability(
    val type: SensorType,
    val isAvailable: Boolean,
    val explicacionCorta: String
)
