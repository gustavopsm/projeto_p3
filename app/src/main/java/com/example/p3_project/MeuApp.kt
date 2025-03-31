package com.example.p3_project

import android.app.Application
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.data.repositories.TorneioRepository

class MeuApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy { TorneioRepository(database.torneioDao()) }
}
