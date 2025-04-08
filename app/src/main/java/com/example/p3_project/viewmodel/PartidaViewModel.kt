package com.example.p3_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.repository.PartidaRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class PartidaViewModel(private val repository: PartidaRepository) : ViewModel() {

    fun getPartidasPorTorneio(torneioId: Long): Flow<List<Partida>> {
        return repository.getPartidasPorTorneio(torneioId)
    }

    fun insert(partida: Partida) {
        viewModelScope.launch {
            repository.insert(partida)
        }
    }

    fun delete(partida: Partida) {
        viewModelScope.launch {
            repository.delete(partida)
        }
    }
}
