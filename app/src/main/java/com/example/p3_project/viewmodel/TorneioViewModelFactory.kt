package com.example.p3_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p3_project.data.repositories.TorneioRepository
import com.example.p3_project.viewmodels.TorneioViewModel

class TorneioViewModelFactory(private val repository: TorneioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TorneioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TorneioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
