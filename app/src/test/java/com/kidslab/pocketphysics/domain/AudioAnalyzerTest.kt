package com.kidslab.pocketphysics.domain

import com.google.common.truth.Truth.assertThat
import com.kidslab.pocketphysics.domain.usecase.AudioAnalyzer
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sin

/**
 * Pruebas del procesamiento de amplitud y frecuencia a partir de bloques
 * PCM SINTÉTICOS (nunca se usa audio real ni se graba nada en los tests).
 */
class AudioAnalyzerTest {

    private fun tonoSintetico(freqHz: Double, amplitud: Int, sampleRate: Int, n: Int): ShortArray {
        return ShortArray(n) { i -> (amplitud * sin(2.0 * PI * freqHz * i / sampleRate)).toInt().toShort() }
    }

    @Test
    fun `pcm vacio produce resultado neutro`() {
        val resultado = AudioAnalyzer.analyze(ShortArray(0), 16000)
        assertThat(resultado.amplitudPromedio).isEqualTo(0f)
        assertThat(resultado.amplitudMaxima).isEqualTo(0f)
        assertThat(resultado.forma).isEmpty()
    }

    @Test
    fun `un sonido fuerte tiene mayor amplitud que uno suave`() {
        val suave = tonoSintetico(440.0, amplitud = 500, sampleRate = 16000, n = 2048)
        val fuerte = tonoSintetico(440.0, amplitud = 15000, sampleRate = 16000, n = 2048)

        val resultadoSuave = AudioAnalyzer.analyze(suave, 16000)
        val resultadoFuerte = AudioAnalyzer.analyze(fuerte, 16000)

        assertThat(resultadoFuerte.amplitudPromedio).isGreaterThan(resultadoSuave.amplitudPromedio)
        assertThat(resultadoFuerte.amplitudMaxima).isGreaterThan(resultadoSuave.amplitudMaxima)
    }

    @Test
    fun `un sonido agudo tiene mayor frecuencia dominante que uno grave`() {
        val grave = tonoSintetico(220.0, amplitud = 10000, sampleRate = 16000, n = 2048)
        val agudo = tonoSintetico(1800.0, amplitud = 10000, sampleRate = 16000, n = 2048)

        val resultadoGrave = AudioAnalyzer.analyze(grave, 16000)
        val resultadoAgudo = AudioAnalyzer.analyze(agudo, 16000)

        assertThat(resultadoAgudo.frecuenciaDominanteHz).isGreaterThan(resultadoGrave.frecuenciaDominanteHz)
    }

    @Test
    fun `la forma de onda se reduce a 64 puntos normalizados entre -1 y 1`() {
        val pcm = tonoSintetico(440.0, amplitud = 20000, sampleRate = 16000, n = 4096)
        val resultado = AudioAnalyzer.analyze(pcm, 16000)

        assertThat(resultado.forma).hasSize(64)
        resultado.forma.forEach { valor ->
            assertThat(valor).isAtLeast(-1f)
            assertThat(valor).isAtMost(1f)
        }
    }

    @Test
    fun `silencio produce amplitud muy baja`() {
        val silencio = ShortArray(2048) { 0 }
        val resultado = AudioAnalyzer.analyze(silencio, 16000)
        assertThat(resultado.amplitudPromedio).isEqualTo(0f)
        assertThat(resultado.amplitudMaxima).isEqualTo(0f)
    }
}
