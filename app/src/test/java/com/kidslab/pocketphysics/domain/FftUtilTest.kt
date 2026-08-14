package com.kidslab.pocketphysics.domain

import com.google.common.truth.Truth.assertThat
import com.kidslab.pocketphysics.domain.usecase.FftUtil
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sin

/**
 * Pruebas de la FFT propia (sin librerías externas) usando señales
 * sintéticas de frecuencia conocida, tal como pide la especificación.
 */
class FftUtilTest {

    @Test
    fun `nextPowerOfTwo redondea hacia arriba correctamente`() {
        assertThat(FftUtil.nextPowerOfTwo(1)).isEqualTo(1)
        assertThat(FftUtil.nextPowerOfTwo(5)).isEqualTo(8)
        assertThat(FftUtil.nextPowerOfTwo(1024)).isEqualTo(1024)
        assertThat(FftUtil.nextPowerOfTwo(1025)).isEqualTo(2048)
    }

    @Test
    fun `detecta la frecuencia dominante de una onda seno sintetica de 440Hz`() {
        val sampleRate = 16000
        val freqObjetivo = 440.0
        val n = 2048
        val señal = DoubleArray(n) { i -> sin(2.0 * PI * freqObjetivo * i / sampleRate) }

        val detectada = FftUtil.dominantFrequency(señal, sampleRate)

        // La resolución de la FFT es sampleRate/n ≈ 7.8 Hz por bin; toleramos un bin de margen.
        assertThat(detectada).isWithin(20f).of(440f)
    }

    @Test
    fun `detecta la frecuencia dominante de una onda seno sintetica de 1000Hz`() {
        val sampleRate = 16000
        val freqObjetivo = 1000.0
        val n = 2048
        val señal = DoubleArray(n) { i -> sin(2.0 * PI * freqObjetivo * i / sampleRate) }

        val detectada = FftUtil.dominantFrequency(señal, sampleRate)
        assertThat(detectada).isWithin(20f).of(1000f)
    }

    @Test
    fun `una frecuencia mas aguda produce un valor detectado mayor que una mas grave`() {
        val sampleRate = 16000
        val n = 2048
        val grave = DoubleArray(n) { i -> sin(2.0 * PI * 200.0 * i / sampleRate) }
        val agudo = DoubleArray(n) { i -> sin(2.0 * PI * 2000.0 * i / sampleRate) }

        val freqGrave = FftUtil.dominantFrequency(grave, sampleRate)
        val freqAgudo = FftUtil.dominantFrequency(agudo, sampleRate)

        assertThat(freqAgudo).isGreaterThan(freqGrave)
    }

    @Test
    fun `señal vacia devuelve cero`() {
        assertThat(FftUtil.dominantFrequency(DoubleArray(0), 16000)).isEqualTo(0f)
    }
}
