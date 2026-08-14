package com.kidslab.pocketphysics.domain.model

/** Una lectura instantánea del acelerómetro, en m/s². */
data class MotionSample(
    val x: Float,
    val y: Float,
    val z: Float,
    val timestampMs: Long
) {
    /** Magnitud del vector de aceleración, útil para graficar un solo valor. */
    val magnitude: Float
        get() = kotlin.math.sqrt(x * x + y * y + z * z)
}

/** Una lectura instantánea del giroscopio, en rad/s por eje. */
data class RotationSample(
    val x: Float,
    val y: Float,
    val z: Float,
    val timestampMs: Long
) {
    val magnitude: Float
        get() = kotlin.math.sqrt(x * x + y * y + z * z)
}

/** Resultado de analizar un bloque de audio PCM (sin guardarlo). */
data class SoundAnalysisResult(
    val amplitudPromedio: Float,
    val amplitudMaxima: Float,
    val frecuenciaDominanteHz: Float,
    val forma: List<Float> // onda simplificada para dibujar (ya normalizada -1..1)
)

/** Una lectura instantánea del sensor de luz, en lux. */
data class LightSample(
    val lux: Float,
    val timestampMs: Long
)

/** Resumen genérico de una serie de mediciones (min/máx/promedio/duración). */
data class SensorSummary(
    val minimo: Float,
    val maximo: Float,
    val promedio: Float,
    val duracionMs: Long,
    val muestras: Int
)
