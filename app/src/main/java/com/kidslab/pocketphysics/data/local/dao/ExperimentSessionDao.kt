package com.kidslab.pocketphysics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.kidslab.pocketphysics.data.local.entity.ExperimentResult
import com.kidslab.pocketphysics.data.local.entity.ExperimentSession
import com.kidslab.pocketphysics.data.local.entity.ExperimentSessionFull
import com.kidslab.pocketphysics.data.local.entity.MeasurementSummary
import com.kidslab.pocketphysics.data.local.entity.Prediction
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperimentSessionDao {

    @Insert
    suspend fun insertSession(session: ExperimentSession): Long

    @Insert
    suspend fun insertPrediction(prediction: Prediction): Long

    @Insert
    suspend fun insertSummary(summary: MeasurementSummary): Long

    @Insert
    suspend fun insertResult(result: ExperimentResult): Long

    /**
     * Guarda una sesión de experimento completa de forma atómica:
     * la sesión, su predicción, el resumen de medición y el resultado.
     * Devuelve el id de la sesión creada.
     */
    @Transaction
    suspend fun saveFullSession(
        session: ExperimentSession,
        predictionText: String,
        summary: MeasurementSummary,
        result: ExperimentResult
    ): Long {
        val sessionId = insertSession(session)
        insertPrediction(Prediction(sessionId = sessionId, textoPrediccion = predictionText))
        insertSummary(summary.copy(sessionId = sessionId))
        insertResult(result.copy(sessionId = sessionId))
        return sessionId
    }

    @Transaction
    @Query("SELECT * FROM experiment_session WHERE profileId = :profileId ORDER BY fecha DESC")
    fun observeSessionsForProfile(profileId: Long): Flow<List<ExperimentSessionFull>>

    @Transaction
    @Query("SELECT * FROM experiment_session WHERE id = :sessionId")
    suspend fun getFullSession(sessionId: Long): ExperimentSessionFull?

    @Query("SELECT COUNT(*) FROM experiment_session WHERE profileId = :profileId AND experimentKey = :experimentKey")
    suspend fun countByExperiment(profileId: Long, experimentKey: String): Int

    @Query("DELETE FROM experiment_session WHERE id = :sessionId")
    suspend fun delete(sessionId: Long)
}
