package com.kidslab.pocketphysics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfile): Long

    @Update
    suspend fun update(profile: UserProfile)

    @Query("SELECT * FROM user_profile ORDER BY id ASC LIMIT 1")
    fun observeFirstProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = :profileId")
    suspend fun getById(profileId: Long): UserProfile?

    @Query("SELECT COUNT(*) FROM user_profile")
    suspend fun count(): Int
}
