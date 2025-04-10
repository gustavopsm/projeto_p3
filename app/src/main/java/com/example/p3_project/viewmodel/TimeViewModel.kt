package com.example.p3_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.repository.TimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TimeViewModel(private val repository: TimeRepository) : ViewModel() {
    val times: Flow<List<Time>> = repository.getAllTimes()

    fun getTimesPorTorneio(torneioId: Int): Flow<List<Time>> {
        return repository.getTimesPorTorneio(torneioId)
    }

    suspend fun insertTime(time: Time) {
        viewModelScope.launch {
            repository.insertTime(time)
        }
    }

    fun updateTime(time: Time) {
        viewModelScope.launch {
            repository.updateTime(time)
        }
    }

    fun deleteTime(time: Time) {
        viewModelScope.launch {
            repository.deleteTime(time)
        }
    }
}