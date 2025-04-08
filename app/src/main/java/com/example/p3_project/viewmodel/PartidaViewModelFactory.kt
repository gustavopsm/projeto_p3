package com.example.p3_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p3_project.data.repository.PartidaRepository
import com.example.p3_project.viewmodels.PartidaViewModel

class PartidaViewModelFactory(private val repository: PartidaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartidaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PartidaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}