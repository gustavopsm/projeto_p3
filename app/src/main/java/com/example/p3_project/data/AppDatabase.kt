package com.example.p3_project.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.p3_project.data.dao.TorneioDao
import com.example.p3_project.data.dao.TimeDao
import com.example.p3_project.data.dao.PartidaDao
import com.example.p3_project.data.dao.UsuarioDao

import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.entities.Usuario

@Database(
    entities = [Torneio::class, Time::class, Partida::class, Usuario::class],
    version = 8,
    exportSchema = false
)

@TypeConverters(Converters::class) // Registrando os Converters
abstract class AppDatabase : RoomDatabase() {
    abstract fun torneioDao(): TorneioDao
    abstract fun timeDao(): TimeDao
    abstract fun partidaDao(): PartidaDao
    abstract fun usuarioDao(): UsuarioDao


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


