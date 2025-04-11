package com.example.p3_project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.repository.TimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimeViewModel(
    private val repository: TimeRepository
) : ViewModel() {

    // ✅ Aqui já criamos o StateFlow corretamente
    private val _times = MutableStateFlow<List<Time>>(emptyList())
    val times: StateFlow<List<Time>> = _times.asStateFlow()

    init {
        carregarTimes()
    }

    private fun carregarTimes() {
        viewModelScope.launch {
            repository.getAllTimes().collect { timesList ->
                _times.value = timesList
            }
        }
    }

    fun insertTime(time: Time) {
        viewModelScope.launch {
            repository.insertTime(time)
            carregarTimes() // Opcional: garante atualização após inserção
        }
    }

    fun deleteTime(time: Time) {
        viewModelScope.launch {
            repository.deleteTime(time)
            carregarTimes() // Opcional
        }
    }
}
