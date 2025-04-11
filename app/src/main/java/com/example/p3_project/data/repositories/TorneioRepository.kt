package com.example.p3_project.data.repositories

import android.util.Log
import com.example.p3_project.data.dao.TorneioDao
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.network.ApiClient
import kotlinx.coroutines.flow.Flow

class TorneioRepository(private val torneiosDao: TorneioDao) {

    private val torneioService = ApiClient.torneioService

    // 🔥 Flow para o app observar em tempo real
    fun getAllTorneios(): Flow<List<Torneio>> {
        return torneiosDao.getAllTorneios()
    }

    // 🔥 Criação de torneio via API + salva local
    suspend fun insert(torneio: Torneio) {
        try {
            val response = torneioService.criarTorneio(torneio)
            if (response.isSuccessful) {
                response.body()?.let { torneioFromApi ->
                    torneiosDao.insert(torneioFromApi)
                    Log.d("TorneioRepository", "Torneio criado na API e salvo localmente com sucesso.")
                }
            } else {
                Log.e("TorneioRepository", "Erro na criação do torneio na API: ${response.errorBody()?.string()}")
                throw Exception("Erro na API: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("TorneioRepository", "Exceção ao criar torneio: ${e.localizedMessage}")
            throw e
        }
    }

    // 🔥 Atualização no banco local
    suspend fun update(torneio: Torneio) {
        torneiosDao.update(torneio)
    }

    // 🔥 Deleção no banco local
    suspend fun delete(torneio: Torneio) {
        torneiosDao.delete(torneio)
    }

    // 🔥 Sincroniza os torneios remotos com o banco local
    suspend fun listarTorneiosRemotos(): List<Torneio>? {
        return try {
            val response = torneioService.listarTorneios()
            if (response.isSuccessful) {
                val torneiosRemotos = response.body() ?: emptyList()

                // Atualiza o banco local para refletir a API remota
                torneiosDao.deleteAll() // Opcional: limpa a tabela antes
                torneiosDao.insertAll(torneiosRemotos)

                Log.d("TorneioRepository", "Torneios sincronizados com sucesso.")

                torneiosRemotos
            } else {
                Log.e("TorneioRepository", "Erro ao listar torneios na API: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
