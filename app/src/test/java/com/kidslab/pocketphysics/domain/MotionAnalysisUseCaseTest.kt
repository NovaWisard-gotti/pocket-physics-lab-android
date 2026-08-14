package com.kidslab.pocketphysics.domain

import com.google.common.truth.Truth.assertThat
import com.kidslab.pocketphysics.domain.model.MotionSample
import com.kidslab.pocketphysics.domain.usecase.MotionAnalysisUseCase
import org.junit.Test

/**
 * Pruebas del análisis de movimiento con datos de acelerómetro SIMULADOS
 * (sin hardware real, sin Android). Cubre resumen (mín/máx/promedio) y
 * detección de "quieto" vs "en movimiento".
 */
class MotionAnalysisUseCaseTest {

    private fun quieto(n: Int): List<MotionSample> = List(n) { i ->
        // Ruido mínimo alrededor de la gravedad (9.81) en el eje Z, típico de un teléfono en reposo.
        MotionSample(x = 0.02f, y = 0.01f, z = 9.80f + (if (i % 2 == 0) 0.02f else -0.02f), timestampMs = i * 20L)
    }

    private fun enMovimiento(n: Int): List<MotionSample> = List(n) { i ->
        MotionSample(x = 4f * kotlin.math.sin(i * 0.5f), y = 2f, z = 9.8f, timestampMs = i * 20L)
    }

    @Test
    fun `resumen calcula minimo maximo y promedio correctos con lista vacia`() {
        val resumen = MotionAnalysisUseCase.summarize(emptyList())
        assertThat(resumen.muestras).isEqualTo(0)
        assertThat(resumen.minimo).isEqualTo(0f)
        assertThat(resumen.maximo).isEqualTo(0f)
    }

    @Test
    fun `resumen calcula minimo maximo y promedio con datos simulados`() {
        val muestras = listOf(
            MotionSample(0f, 0f, 1f, 0),
            MotionSample(0f, 0f, 3f, 20),
            MotionSample(0f, 0f, 5f, 40)
        )
        val resumen = MotionAnalysisUseCase.summarize(muestras)
        assertThat(resumen.minimo).isEqualTo(1f)
        assertThat(resumen.maximo).isEqualTo(5f)
        assertThat(resumen.promedio).isEqualTo(3f)
        assertThat(resumen.muestras).isEqualTo(3)
        assertThat(resumen.duracionMs).isEqualTo(40)
    }

    @Test
    fun `detecta telefono quieto con ruido bajo alrededor de la gravedad`() {
        val muestras = quieto(50)
        assertThat(MotionAnalysisUseCase.estuvoQuieto(muestras)).isTrue()
    }

    @Test
    fun `detecta movimiento cuando la magnitud varia mucho`() {
        val muestras = enMovimiento(50)
        assertThat(MotionAnalysisUseCase.estuvoQuieto(muestras)).isFalse()
    }

    @Test
    fun `rango de movimiento es mayor cuando hay movimiento que cuando esta quieto`() {
        val rangoQuieto = MotionAnalysisUseCase.rangoDeMovimiento(quieto(50))
        val rangoMovimiento = MotionAnalysisUseCase.rangoDeMovimiento(enMovimiento(50))
        assertThat(rangoMovimiento).isGreaterThan(rangoQuieto)
    }
}
