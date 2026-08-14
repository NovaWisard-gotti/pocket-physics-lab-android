package com.kidslab.pocketphysics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorExperimentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(experiments: List<SensorExperiment>)

    @Query("SELECT * FROM sensor_experiment ORDER BY ordenSugerido ASC")
    fun observeAll(): Flow<List<SensorExperiment>>

    @Query("SELECT * FROM sensor_experiment WHERE labKey = :labKey ORDER BY ordenSugerido ASC")
    fun observeByLab(labKey: String): Flow<List<SensorExperiment>>

    @Query("SELECT * FROM sensor_experiment WHERE experimentKey = :key")
    suspend fun getByKey(key: String): SensorExperiment?

    @Query("SELECT COUNT(*) FROM sensor_experiment")
    suspend fun count(): Int
}
