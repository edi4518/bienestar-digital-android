package com.example.bienestar_digital_android.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AuditoriaDiariaEntity::class,
        UserProfileEntity::class,
        MinijuegoRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BienestarDatabase : RoomDatabase() {

    abstract fun auditoriaDao(): AuditoriaDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun minijuegoRecordDao(): MinijuegoRecordDao

    companion object {
        @Volatile
        private var INSTANCE: BienestarDatabase? = null

        fun getDatabase(context: Context): BienestarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BienestarDatabase::class.java,
                    "bienestar_digital_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
