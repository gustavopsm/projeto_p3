package com.example.p3_project

import android.app.Application
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.repositories.TorneioRepository
import com.example.p3_project.data.repository.TimeRepository

class MeuApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val torneioRepository by lazy { TorneioRepository(database.torneioDao()) }
    val timeRepository by lazy { TimeRepository(database.timeDao()) }
}