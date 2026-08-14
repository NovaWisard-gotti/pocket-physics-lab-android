package com.kidslab.pocketphysics.data.repository

import com.kidslab.pocketphysics.data.local.dao.ExperimentSessionDao
import com.kidslab.pocketphysics.data.local.dao.SensorExperimentDao
import com.kidslab.pocketphysics.data.local.entity.ExperimentResult
import com.kidslab.pocketphysics.data.local.entity.ExperimentSession
import com.kidslab.pocketphysics.data.local.entity.ExperimentSessionFull
import com.kidslab.pocketphysics.data.local.entity.MeasurementSummary
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.domain.model.SensorSummary
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio del catálogo de experimentos guiados y del registro científico
 * (sesiones ya realizadas, con predicción, resumen y resultado).
 */
class ExperimentRepository(
    private val experimentDao: SensorExperimentDao,
    private val sessionDao: ExperimentSessionDao
) {
    fun observeCatalog(): Flow<List<SensorExperiment>> = experimentDao.observeAll()

    fun observeCatalogByLab(labKey: String): Flow<List<SensorExperiment>> = experimentDao.observeByLab(labKey)

    suspend fun getExperiment(key: String): SensorExperiment? = experimentDao.getByKey(key)

    fun observeSessionsForProfile(profileId: Long): Flow<List<ExperimentSessionFull>> =
        sessionDao.observeSessionsForProfile(profileId)

    suspend fun countSessions(profileId: Long, experimentKey: String): Int =
        sessionDao.countByExperiment(profileId, experimentKey)

    /**
     * Guarda una sesión completa: predicción del usuario, resumen calculado a
     * partir de los datos de sensor, y resultado/conclusión. No se guardan
     * las muestras individuales, solo el resumen agregado.
     */
    suspend fun saveSession(
        profileId: Long,
        experimentKey: String,
        sensorType: String,
        predictionText: String,
        summary: SensorSummary,
        prediccionCorrecta: Boolean,
        textoResultado: String,
        textoConclusion: String
    ): Long {
        val session = ExperimentSession(
            profileId = profileId,
            experimentKey = experimentKey,
            fecha = System.currentTimeMillis(),
            durationMs = summary.duracionMs
        )
        val measurementSummary = MeasurementSummary(
            sessionId = 0, // se reemplaza dentro de saveFullSession
            sensorType = sensorType,
            valorMinimo = summary.minimo,
            valorMaximo = summary.maximo,
            valorPromedio = summary.promedio,
            duracionMs = summary.duracionMs,
            muestrasAnalizadas = summary.muestras
        )
        val result = ExperimentResult(
            sessionId = 0,
            prediccionCorrecta = prediccionCorrecta,
            textoResultado = textoResultado,
            textoConclusion = textoConclusion
        )
        return sessionDao.saveFullSession(session, predictionText, measurementSummary, result)
    }

    suspend fun deleteSession(sessionId: Long) = sessionDao.delete(sessionId)
}
