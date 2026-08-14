package com.kidslab.pocketphysics.domain

import com.google.common.truth.Truth.assertThat
import com.kidslab.pocketphysics.domain.model.RotationSample
import com.kidslab.pocketphysics.domain.usecase.RotationAnalysisUseCase
import org.junit.Test
import kotlin.math.PI

class RotationAnalysisUseCaseTest {

    @Test
    fun `resumen con lista vacia devuelve ceros`() {
        val resumen = RotationAnalysisUseCase.summarize(emptyList())
        assertThat(resumen.muestras).isEqualTo(0)
    }

    @Test
    fun `angulo estimado es cero con una sola muestra`() {
        val muestras = listOf(RotationSample(0f, 0f, 1f, 0))
        assertThat(RotationAnalysisUseCase.anguloEstimadoGrados(muestras)).isEqualTo(0f)
    }

    @Test
    fun `angulo estimado se aproxima a 90 grados con velocidad angular constante`() {
        // Velocidad angular constante de PI/2 rad/s en el eje Z durante 1 segundo = 90 grados.
        val velocidadRadPorSeg = (PI / 2).toFloat()
        val muestras = (0..20).map { i ->
            RotationSample(0f, 0f, velocidadRadPorSeg, i * 50L) // 50ms entre muestras, 21 muestras = 1s
        }
        val angulo = RotationAnalysisUseCase.anguloEstimadoGrados(muestras)
        assertThat(angulo).isWithin(5f).of(90f)
    }

    @Test
    fun `resumen de rotacion calcula minimo maximo y promedio de la magnitud`() {
        val muestras = listOf(
            RotationSample(1f, 0f, 0f, 0),
            RotationSample(2f, 0f, 0f, 20),
            RotationSample(3f, 0f, 0f, 40)
        )
        val resumen = RotationAnalysisUseCase.summarize(muestras)
        assertThat(resumen.minimo).isEqualTo(1f)
        assertThat(resumen.maximo).isEqualTo(3f)
        assertThat(resumen.promedio).isEqualTo(2f)
    }
}
