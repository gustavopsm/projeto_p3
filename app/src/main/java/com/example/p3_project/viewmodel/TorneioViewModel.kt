package com.example.p3_project.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.entities.TipoTorneio
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.repositories.TorneioRepository
import com.example.p3_project.data.repository.PartidaRepository
import com.example.p3_project.domain.TorneioManager

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class TorneioViewModel(
    private val repository: TorneioRepository,
    private val torneioManager: TorneioManager,
    private val partidaRepository: PartidaRepository
    ) : ViewModel() {

    private val _torneios = MutableLiveData<List<Torneio>>()
    val torneios: LiveData<List<Torneio>> = _torneios


    fun carregarTorneios() {
        viewModelScope.launch {
            val torneiosRemotos = repository.listarTorneiosRemotos()
            torneiosRemotos?.let {
                _torneios.postValue(it)
            }
        }
    }

//    suspend fun getTorneiosList(): List<Torneio> {
//        return torneios.firstOrNull() ?: emptyList()
//    }

    fun insert(torneio: Torneio) {
        viewModelScope.launch {
            repository.insert(torneio)
            Log.d("TorneioViewModel", "Torneio inserido: $torneio")
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

    fun iniciarTorneio(torneioId: Long, times: List<Time>, tipoTorneio: TipoTorneio) {
        viewModelScope.launch {
            try {
                torneioManager.iniciarTorneio(torneioId, times, tipoTorneio as com.example.p3_project.domain.TipoTorneio)
                Log.d("Torneio", "Torneio iniciado com sucesso!")
            } catch (e: Exception) {
                Log.e("Torneio", "Erro ao iniciar torneio: ${e.message}")
            }
        }
    }

    fun getPartidasPorTorneio(torneioId: Long): Flow<List<Partida>> {
        return partidaRepository.getPartidasPorTorneio(torneioId)
    }
}