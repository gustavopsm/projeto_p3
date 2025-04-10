package com.example.p3_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p3_project.data.repositories.TorneioRepository
import com.example.p3_project.data.repository.PartidaRepository
import com.example.p3_project.domain.TorneioManager
import com.example.p3_project.viewmodels.TorneioViewModel

class TorneioViewModelFactory(
    private val repository: TorneioRepository,
    private val torneioManager: TorneioManager,
    private val partidaRepository: PartidaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TorneioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TorneioViewModel(repository, torneioManager, partidaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

