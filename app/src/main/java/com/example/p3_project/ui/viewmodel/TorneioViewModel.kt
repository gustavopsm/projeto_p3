package com.example.p3_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.repositories.TorneioRepository
import kotlinx.coroutines.launch

class TorneioViewModel(private val repository: TorneioRepository) : ViewModel() {

    val torneios = repository.getAllTorneios()

    fun insert(torneio: Torneio) {
        viewModelScope.launch {
            repository.insert(torneio)
        }
    }

    fun update(torneio: Torneio) {
        viewModelScope.launch {
            repository.update(torneio)
        }
    }

    fun delete(torneio: Torneio) {
        viewModelScope.launch {
            repository.delete(torneio)
        }
    }
}