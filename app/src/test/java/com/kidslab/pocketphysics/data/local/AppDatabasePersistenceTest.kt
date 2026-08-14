package com.kidslab.pocketphysics.data.local

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kidslab.pocketphysics.data.local.entity.ExperimentResult
import com.kidslab.pocketphysics.data.local.entity.ExperimentSession
import com.kidslab.pocketphysics.data.local.entity.MeasurementSummary
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Prueba de persistencia usando una base de datos Room EN MEMORIA
 * (sin disco, sin hardware), corrida con Robolectric. Verifica que:
 * - Se puede guardar un perfil, un experimento del catálogo y una sesión completa.
 * - La sesión guardada solo contiene el RESUMEN (min/máx/promedio), no muestras crudas.
 */
@RunWith(RobolectricTestRunner::class)
class AppDatabasePersistenceTest {

    private lateinit var db: AppDatabase

    @Before
    fun crearBaseEnMemoria() {
        db = AppDatabase.buildInMemory(ApplicationProvider.getApplicationContext())
    }

    @After
    fun cerrarBase() {
        db.close()
    }

    @Test
    fun `guardar y recuperar una sesion completa con resumen`() = runTest {
        val profileId = db.userProfileDao().insert(UserProfile(name = "Ada", avatarKey = "atomo", createdAt = 0L))
        db.sensorExperimentDao().insertAll(
            listOf(SensorExperiment("mov_quieto", "ACCELEROMETER", "movimiento", "El teléfono en reposo", "¿Qué marca el acelerómetro quieto?", 1))
        )

        val sessionId = db.experimentSessionDao().saveFullSession(
            session = ExperimentSession(profileId = profileId, experimentKey = "mov_quieto", fecha = 1000L, durationMs = 5000L),
            predictionText = "Creo que marcará cero",
            summary = MeasurementSummary(
                sessionId = 0, sensorType = "ACCELEROMETER",
                valorMinimo = 9.7f, valorMaximo = 9.9f, valorPromedio = 9.8f, duracionMs = 5000L, muestrasAnalizadas = 120
            ),
            result = ExperimentResult(sessionId = 0, prediccionCorrecta = true, textoResultado = "Se mantuvo quieto", textoConclusion = "La gravedad domina la lectura")
        )

        assertThat(sessionId).isGreaterThan(0L)

        val recuperada = db.experimentSessionDao().getFullSession(sessionId)
        assertThat(recuperada).isNotNull()
        assertThat(recuperada!!.session.experimentKey).isEqualTo("mov_quieto")
        assertThat(recuperada.prediction?.textoPrediccion).isEqualTo("Creo que marcará cero")
        assertThat(recuperada.summary?.valorPromedio).isEqualTo(9.8f)
        assertThat(recuperada.summary?.muestrasAnalizadas).isEqualTo(120)
        assertThat(recuperada.result?.textoConclusion).isEqualTo("La gravedad domina la lectura")
        assertThat(recuperada.experiment?.titulo).isEqualTo("El teléfono en reposo")
    }

    @Test
    fun `las sesiones de un perfil se listan ordenadas por fecha descendente`() = runTest {
        val profileId = db.userProfileDao().insert(UserProfile(name = "Ada", avatarKey = "atomo", createdAt = 0L))
        db.sensorExperimentDao().insertAll(
            listOf(SensorExperiment("mov_quieto", "ACCELEROMETER", "movimiento", "Título", "¿Pregunta?", 1))
        )
        val resumen = MeasurementSummary(
            sessionId = 0, sensorType = "ACCELEROMETER",
            valorMinimo = 0f, valorMaximo = 1f, valorPromedio = 0.5f,
            duracionMs = 1000L, muestrasAnalizadas = 10
        )
        val resultado = ExperimentResult(
            sessionId = 0, prediccionCorrecta = true,
            textoResultado = "ok", textoConclusion = "ok"
        )

        db.experimentSessionDao().saveFullSession(
            ExperimentSession(profileId = profileId, experimentKey = "mov_quieto", fecha = 100L, durationMs = 1000L), "pred1", resumen, resultado
        )
        db.experimentSessionDao().saveFullSession(
            ExperimentSession(profileId = profileId, experimentKey = "mov_quieto", fecha = 200L, durationMs = 1000L), "pred2", resumen, resultado
        )

        val sesiones = db.experimentSessionDao().observeSessionsForProfile(profileId)
        val lista = sesiones.first()
        assertThat(lista).hasSize(2)
        // La más reciente (fecha=200) debe aparecer primero.
        assertThat(lista.first().session.fecha).isEqualTo(200L)
    }
}
