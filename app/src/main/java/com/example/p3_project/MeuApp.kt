package com.example.p3_project

import android.app.Application
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.repositories.TorneioRepository
import com.example.p3_project.data.repository.TimeRepository
import com.example.p3_project.data.repository.PartidaRepository

class MeuApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val torneioRepository by lazy { TorneioRepository(database.torneioDao()) }
    val timeRepository by lazy { TimeRepository(database.timeDao()) }
    val partidaRepository by lazy { PartidaRepository(database.partidaDao()) }
}