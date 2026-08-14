package com.kidslab.pocketphysics.domain.usecase

import com.kidslab.pocketphysics.domain.model.SoundAnalysisResult
import kotlin.math.abs
import kotlin.math.max

/**
 * Analiza un bloque de audio PCM de 16 bits en memoria y lo resume en un
 * [SoundAnalysisResult]. El bloque de muestras (ShortArray) se descarta
 * apenas termina el análisis: nunca se escribe a disco.
 *
 * Son funciones puras (sin AudioRecord, sin Android) para poder probarlas
 * con señales sintéticas, sin hardware.
 */
object AudioAnalyzer {

    private const val PUNTOS_ONDA = 64

    fun analyze(pcm: ShortArray, sampleRateHz: Int): SoundAnalysisResult {
        if (pcm.isEmpty()) {
            return SoundAnalysisResult(0f, 0f, 0f, emptyList())
        }

        var sum = 0.0
        var maxAbs = 0
        for (s in pcm) {
            sum += abs(s.toInt())
            maxAbs = max(maxAbs, abs(s.toInt()))
        }
        val amplitudPromedio = (sum / pcm.size).toFloat()
        val amplitudMaxima = maxAbs.toFloat()

        val samplesAsDouble = DoubleArray(pcm.size) { pcm[it].toDouble() / Short.MAX_VALUE }
        val frecuencia = FftUtil.dominantFrequency(samplesAsDouble, sampleRateHz)

        val forma = downsampleForWaveform(pcm, PUNTOS_ONDA)

        return SoundAnalysisResult(
            amplitudPromedio = amplitudPromedio,
            amplitudMaxima = amplitudMaxima,
            frecuenciaDominanteHz = frecuencia,
            forma = forma
        )
    }

    /** Reduce el bloque de audio a [puntos] valores normalizados entre -1 y 1, para dibujar. */
    private fun downsampleForWaveform(pcm: ShortArray, puntos: Int): List<Float> {
        if (pcm.size <= puntos) {
            return pcm.map { it.toFloat() / Short.MAX_VALUE }
        }
        val chunkSize = pcm.size / puntos
        return (0 until puntos).map { i ->
            val start = i * chunkSize
            val end = minOf(start + chunkSize, pcm.size)
            var acc = 0f
            for (k in start until end) acc += pcm[k]
            (acc / (end - start)) / Short.MAX_VALUE
        }
    }
}
