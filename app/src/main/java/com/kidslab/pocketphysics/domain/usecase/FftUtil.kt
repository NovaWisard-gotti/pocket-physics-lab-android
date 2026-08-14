package com.kidslab.pocketphysics.domain.usecase

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * FFT (Transformada Rápida de Fourier) mínima, escrita a mano en Kotlin puro.
 * No se añade ninguna librería externa de procesamiento de señales solo para
 * esto, tal como pide la especificación del proyecto.
 *
 * Implementación iterativa de Cooley-Tukey (radix-2), solo funciona con
 * tamaños potencia de 2. [FftUtil.nextPowerOfTwo] ayuda a elegir el tamaño
 * de ventana adecuado.
 */
object FftUtil {

    fun nextPowerOfTwo(n: Int): Int {
        var p = 1
        while (p < n) p = p shl 1
        return p
    }

    /**
     * Calcula la FFT in-place de las partes real e imaginaria.
     * [real] e [imag] deben tener tamaño potencia de 2 y la misma longitud.
     */
    fun transform(real: DoubleArray, imag: DoubleArray) {
        val n = real.size
        require(n and (n - 1) == 0) { "El tamaño debe ser potencia de 2, era $n" }
        if (n <= 1) return

        // Bit-reversal
        var j = 0
        for (i in 0 until n - 1) {
            if (i < j) {
                real[i] = real[j].also { real[j] = real[i] }
                imag[i] = imag[j].also { imag[j] = imag[i] }
            }
            var m = n shr 1
            while (m in 1..j) {
                j -= m
                m = m shr 1
            }
            j += m
        }

        // Mariposas Cooley-Tukey
        var len = 2
        while (len <= n) {
            val ang = -2.0 * PI / len
            val wReal = cos(ang)
            val wImag = sin(ang)
            var i = 0
            while (i < n) {
                var curReal = 1.0
                var curImag = 0.0
                for (k in 0 until len / 2) {
                    val evenIdx = i + k
                    val oddIdx = i + k + len / 2
                    val tReal = curReal * real[oddIdx] - curImag * imag[oddIdx]
                    val tImag = curReal * imag[oddIdx] + curImag * real[oddIdx]
                    real[oddIdx] = real[evenIdx] - tReal
                    imag[oddIdx] = imag[evenIdx] - tImag
                    real[evenIdx] += tReal
                    imag[evenIdx] += tImag
                    val nextReal = curReal * wReal - curImag * wImag
                    val nextImag = curReal * wImag + curImag * wReal
                    curReal = nextReal
                    curImag = nextImag
                }
                i += len
            }
            len = len shl 1
        }
    }

    /**
     * Devuelve la frecuencia dominante (en Hz) de una señal real muestreada
     * a [sampleRateHz]. Ignora la componente 0 Hz (offset de continua).
     */
    fun dominantFrequency(samples: DoubleArray, sampleRateHz: Int): Float {
        if (samples.isEmpty()) return 0f
        val size = nextPowerOfTwo(samples.size)
        val real = DoubleArray(size)
        val imag = DoubleArray(size)
        // Ventana de Hann para reducir fugas espectrales
        for (i in samples.indices) {
            val window = 0.5 - 0.5 * cos(2.0 * PI * i / (samples.size - 1).coerceAtLeast(1))
            real[i] = samples[i] * window
        }

        transform(real, imag)

        var bestBin = 1
        var bestMagnitude = -1.0
        // Solo miramos hasta la mitad (frecuencia de Nyquist)
        for (bin in 1 until size / 2) {
            val magnitude = real[bin] * real[bin] + imag[bin] * imag[bin]
            if (magnitude > bestMagnitude) {
                bestMagnitude = magnitude
                bestBin = bin
            }
        }
        return bestBin * sampleRateHz.toFloat() / size
    }
}
