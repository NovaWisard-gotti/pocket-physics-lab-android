package com.kidslab.pocketphysics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.data.local.entity.ChallengeCompletion
import com.kidslab.pocketphysics.data.local.entity.UserBadge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(challenges: List<Challenge>)

    @Query("SELECT * FROM challenge")
    fun observeAll(): Flow<List<Challenge>>

    @Query("SELECT COUNT(*) FROM challenge")
    suspend fun count(): Int
}

@Dao
interface ChallengeCompletionDao {
    @Insert
    suspend fun insert(completion: ChallengeCompletion): Long

    @Query("SELECT * FROM challenge_completion WHERE profileId = :profileId")
    fun observeForProfile(profileId: Long): Flow<List<ChallengeCompletion>>

    @Query("SELECT COUNT(*) FROM challenge_completion WHERE profileId = :profileId AND challengeKey = :challengeKey")
    suspend fun countCompletion(profileId: Long, challengeKey: String): Int
}

@Dao
interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(badges: List<Badge>)

    @Query("SELECT * FROM badge")
    fun observeAll(): Flow<List<Badge>>

    @Query("SELECT COUNT(*) FROM badge")
    suspend fun count(): Int
}

@Dao
interface UserBadgeDao {
    @Insert
    suspend fun insert(userBadge: UserBadge): Long

    @Query("SELECT * FROM user_badge WHERE profileId = :profileId")
    fun observeForProfile(profileId: Long): Flow<List<UserBadge>>

    @Query("SELECT COUNT(*) FROM user_badge WHERE profileId = :profileId AND badgeKey = :badgeKey")
    suspend fun countBadge(profileId: Long, badgeKey: String): Int
}
