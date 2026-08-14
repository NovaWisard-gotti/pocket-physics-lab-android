package com.kidslab.pocketphysics.domain.usecase

import com.kidslab.pocketphysics.domain.model.RotationSample
import com.kidslab.pocketphysics.domain.model.SensorSummary
import kotlin.math.abs

/**
 * Convierte lecturas del giroscopio (velocidad angular en rad/s) en un
 * resumen y en una estimación simplificada del ángulo total girado, usando
 * una integración rectangular sencilla adecuada para un laboratorio
 * educativo (no para uso profesional).
 */
object RotationAnalysisUseCase {

    fun summarize(samples: List<RotationSample>): SensorSummary {
        if (samples.isEmpty()) {
            return SensorSummary(minimo = 0f, maximo = 0f, promedio = 0f, duracionMs = 0, muestras = 0)
        }
        val magnitudes = samples.map { it.magnitude }
        val duracion = (samples.last().timestampMs - samples.first().timestampMs).coerceAtLeast(0)
        return SensorSummary(
            minimo = magnitudes.min(),
            maximo = magnitudes.max(),
            promedio = magnitudes.average().toFloat(),
            duracionMs = duracion,
            muestras = samples.size
        )
    }

    /**
     * Estimación en grados del giro total alrededor del eje Z, integrando la
     * velocidad angular a lo largo del tiempo entre muestras consecutivas.
     * Es una aproximación educativa, no una medición de precisión.
     */
    fun anguloEstimadoGrados(samples: List<RotationSample>): Float {
        if (samples.size < 2) return 0f
        var acumuladoRadianes = 0.0
        for (i in 1 until samples.size) {
            val dtSeconds = (samples[i].timestampMs - samples[i - 1].timestampMs) / 1000.0
            if (dtSeconds <= 0) continue
            acumuladoRadianes += abs(samples[i].z) * dtSeconds
        }
        return (acumuladoRadianes * 180.0 / Math.PI).toFloat()
    }
}
