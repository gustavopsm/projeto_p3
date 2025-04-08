package com.example.p3_project.data.repository

import com.example.p3_project.data.dao.PartidaDao
import com.example.p3_project.data.entities.Partida
import kotlinx.coroutines.flow.Flow

class PartidaRepository(private val partidaDao: PartidaDao) {

    val todasAsPartidas: Flow<List<Partida>> = partidaDao.getAllPartidas()

    fun getPartidasPorTorneio(torneioId: Long): Flow<List<Partida>> {
        return partidaDao.getPartidasPorTorneio(torneioId)
    }

    suspend fun insert(partida: Partida) {
        partidaDao.insert(partida)
    }

    suspend fun delete(partida: Partida) {
        partidaDao.delete(partida)
    }

    suspend fun update(partida: Partida) {
        partidaDao.update((partida))
    }
}
