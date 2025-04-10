package com.example.p3_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p3_project.data.repositories.UsuarioRepository
import com.example.p3_project.data.AppDatabase

class AuthViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val usuarioDao = database.usuarioDao()
            return AuthViewModel(UsuarioRepository(usuarioDao)) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
