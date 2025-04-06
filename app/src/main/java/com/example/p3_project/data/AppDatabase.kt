package com.example.p3_project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.p3_project.data.dao.TorneioDao
import com.example.p3_project.data.entities.Torneio

@Database(
    entities = [Torneio::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun torneioDao(): TorneioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "torneio_db"
                ).setQueryCallback({ sqlQuery, bindArgs ->
                    android.util.Log.d("RoomDB", "Query: $sqlQuery SQL Args: $bindArgs")
                }, java.util.concurrent.Executors.newSingleThreadExecutor())
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
