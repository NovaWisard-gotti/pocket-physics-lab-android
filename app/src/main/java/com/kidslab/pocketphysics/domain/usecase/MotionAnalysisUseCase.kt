package com.kidslab.pocketphysics.domain.usecase

import com.kidslab.pocketphysics.domain.model.MotionSample
import com.kidslab.pocketphysics.domain.model.SensorSummary

/**
 * Convierte una lista de lecturas del acelerómetro en un resumen (min/máx/
 * promedio/duración) y decide si el teléfono estuvo "quieto" o "en
 * movimiento", usando la magnitud del vector de aceleración.
 *
 * Funciones puras, sin dependencias de Android: se pueden probar con datos
 * simulados en tests unitarios de JVM.
 */
object MotionAnalysisUseCase {

    /** Por debajo de esta variación (m/s²) respecto a la gravedad, consideramos "quieto". */
    private const val UMBRAL_QUIETO = 0.35f
    private const val GRAVEDAD_APROX = 9.81f

    fun summarize(samples: List<MotionSample>): SensorSummary {
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
     * true si la variación de la magnitud respecto a la gravedad (9.81 m/s²)
     * se mantuvo por debajo del umbral durante toda la muestra: el teléfono
     * estuvo prácticamente quieto.
     */
    fun estuvoQuieto(samples: List<MotionSample>): Boolean {
        if (samples.isEmpty()) return true
        return samples.all { kotlin.math.abs(it.magnitude - GRAVEDAD_APROX) < UMBRAL_QUIETO }
    }

    /** Diferencia entre el valor máximo y mínimo de magnitud: cuánto "se movió". */
    fun rangoDeMovimiento(samples: List<MotionSample>): Float {
        if (samples.isEmpty()) return 0f
        val magnitudes = samples.map { it.magnitude }
        return magnitudes.max() - magnitudes.min()
    }
}
