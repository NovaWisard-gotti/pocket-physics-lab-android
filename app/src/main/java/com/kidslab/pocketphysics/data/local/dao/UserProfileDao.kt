package com.kidslab.pocketphysics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfile): Long

    /** Todos los perfiles guardados en este teléfono, del más antiguo al más nuevo. */
    @Query("SELECT * FROM user_profile ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<UserProfile>>

    @Query("SELECT * FROM user_profile WHERE id = :profileId")
    fun observeById(profileId: Long): Flow<UserProfile?>
}
