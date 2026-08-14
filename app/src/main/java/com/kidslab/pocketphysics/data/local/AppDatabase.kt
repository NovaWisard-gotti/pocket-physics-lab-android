package com.kidslab.pocketphysics.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kidslab.pocketphysics.data.local.dao.BadgeDao
import com.kidslab.pocketphysics.data.local.dao.ChallengeCompletionDao
import com.kidslab.pocketphysics.data.local.dao.ChallengeDao
import com.kidslab.pocketphysics.data.local.dao.ExperimentSessionDao
import com.kidslab.pocketphysics.data.local.dao.SensorExperimentDao
import com.kidslab.pocketphysics.data.local.dao.UserBadgeDao
import com.kidslab.pocketphysics.data.local.dao.UserProfileDao
import com.kidslab.pocketphysics.data.local.entity.Badge
import com.kidslab.pocketphysics.data.local.entity.Challenge
import com.kidslab.pocketphysics.data.local.entity.ChallengeCompletion
import com.kidslab.pocketphysics.data.local.entity.ExperimentResult
import com.kidslab.pocketphysics.data.local.entity.ExperimentSession
import com.kidslab.pocketphysics.data.local.entity.MeasurementSummary
import com.kidslab.pocketphysics.data.local.entity.Prediction
import com.kidslab.pocketphysics.data.local.entity.SensorExperiment
import com.kidslab.pocketphysics.data.local.entity.UserBadge
import com.kidslab.pocketphysics.data.local.entity.UserProfile
import com.kidslab.pocketphysics.data.local.seed.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Base de datos local de Física de Bolsillo. 100% offline (sin Internet).
 *
 * Guarda perfiles, el catálogo de experimentos guiados, las sesiones que el
 * usuario va completando (con predicción, resumen de medición y resultado),
 * desafíos e insignias.
 *
 * No se guarda audio crudo ni miles de muestras de sensor: solo resúmenes.
 */
@Database(
    entities = [
        UserProfile::class,
        SensorExperiment::class,
        ExperimentSession::class,
        Prediction::class,
        MeasurementSummary::class,
        ExperimentResult::class,
        Challenge::class,
        ChallengeCompletion::class,
        Badge::class,
        UserBadge::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun sensorExperimentDao(): SensorExperimentDao
    abstract fun experimentSessionDao(): ExperimentSessionDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun challengeCompletionDao(): ChallengeCompletionDao
    abstract fun badgeDao(): BadgeDao
    abstract fun userBadgeDao(): UserBadgeDao

    companion object {
        private const val DB_NAME = "pocket_physics_lab.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context, scope).also { INSTANCE = it }
            }
        }

        private fun build(context: Context, scope: CoroutineScope): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch {
                            INSTANCE?.let { database ->
                                database.sensorExperimentDao().insertAll(SeedData.experiments())
                                database.challengeDao().insertAll(SeedData.challenges())
                                database.badgeDao().insertAll(SeedData.badges())
                            }
                        }
                    }
                })
                .build()
        }

        /** Construye una instancia en memoria, útil para tests sin hardware ni disco. */
        fun buildInMemory(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        }
    }
}
