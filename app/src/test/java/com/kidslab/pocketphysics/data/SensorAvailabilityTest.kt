package com.kidslab.pocketphysics.data

import com.google.common.truth.Truth.assertThat
import com.kidslab.pocketphysics.data.sensors.FakeSensorRepository
import com.kidslab.pocketphysics.domain.model.SensorType
import com.kidslab.pocketphysics.ui.screens.sensors.buildSensorAvailability
import org.junit.Test

/**
 * Prueba el caso de un sensor inexistente en el dispositivo (por ejemplo,
 * un teléfono sin sensor de luz o sin giroscopio), usando la abstracción
 * [com.kidslab.pocketphysics.data.repository.SensorRepository] con una
 * implementación falsa: no se necesita hardware real.
 */
class SensorAvailabilityTest {

    @Test
    fun `buildSensorAvailability marca como no disponible el sensor faltante`() {
        val disponibilidad = buildSensorAvailability(
            hasAccelerometer = true,
            hasGyroscope = false, // dispositivo sin giroscopio
            hasMicrophone = true,
            hasLight = false // dispositivo sin sensor de luz
        )

        val giroscopio = disponibilidad.first { it.type == SensorType.GYROSCOPE }
        val luz = disponibilidad.first { it.type == SensorType.LIGHT }
        val acelerometro = disponibilidad.first { it.type == SensorType.ACCELEROMETER }

        assertThat(giroscopio.isAvailable).isFalse()
        assertThat(luz.isAvailable).isFalse()
        assertThat(acelerometro.isAvailable).isTrue()
    }

    @Test
    fun `cada sensor incluye una explicacion no vacia`() {
        val disponibilidad = buildSensorAvailability(true, true, true, true)
        disponibilidad.forEach { assertThat(it.explicacionCorta).isNotEmpty() }
    }

    @Test
    fun `FakeSensorRepository reporta correctamente un sensor no disponible`() {
        val repo = FakeSensorRepository(
            availability = mapOf(
                SensorType.ACCELEROMETER to true,
                SensorType.GYROSCOPE to false,
                SensorType.MICROPHONE to true,
                SensorType.LIGHT to false
            )
        )
        assertThat(repo.isSensorAvailable(SensorType.GYROSCOPE)).isFalse()
        assertThat(repo.isSensorAvailable(SensorType.LIGHT)).isFalse()
        assertThat(repo.isSensorAvailable(SensorType.ACCELEROMETER)).isTrue()
    }

    @Test
    fun `FakeSensorRepository sin datos configurados no falla al observar`() = kotlinx.coroutines.test.runTest {
        val repo = FakeSensorRepository(availability = mapOf(SensorType.GYROSCOPE to false))
        val muestras = repo.observeGyroscope().toListSafely()
        assertThat(muestras).isEmpty()
    }

    private suspend fun <T> kotlinx.coroutines.flow.Flow<T>.toListSafely(): List<T> {
        val resultado = mutableListOf<T>()
        this.collect { resultado.add(it) }
        return resultado
    }
}
