package com.example.p3_project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.p3_project.data.dao.TorneioDao
import com.example.p3_project.data.dao.TimeDao

import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.entities.Time

@Database(
    entities = [Torneio::class, Time::class],
    version = 2,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun torneioDao(): TorneioDao
    abstract fun timeDao(): TimeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "torneio_db"
                )
                    .fallbackToDestructiveMigration() // ⬅️ Para recriar o banco ao mudar a versão
                    .setQueryCallback({ sqlQuery, bindArgs ->
                        android.util.Log.d("RoomDB", "Query: $sqlQuery SQL Args: $bindArgs")
                    }, java.util.concurrent.Executors.newSingleThreadExecutor())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
