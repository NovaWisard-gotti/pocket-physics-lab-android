package com.kidslab.pocketphysics.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Registro de que un perfil completó un desafío concreto, opcionalmente
 * enlazado a la sesión de experimento que lo logró.
 */
@Entity(
    tableName = "challenge_completion",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Challenge::class,
            parentColumns = ["challengeKey"],
            childColumns = ["challengeKey"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("challengeKey")]
)
data class ChallengeCompletion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val challengeKey: String,
    val sessionId: Long?,
    val completedAt: Long
)
