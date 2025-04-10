package com.example.p3_project.data.services

import com.example.p3_project.data.entities.FaseTorneio
import com.example.p3_project.data.entities.Partida
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class GerenciadorDeSorteio {

    suspend fun sortearPartidas(times: List<Long>, torneioId: Long): List<Partida> {
        return withContext(Dispatchers.Default) {
            val partidas = mutableListOf<Partida>()
            val totalTimes = times.size

            val formatoData = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            for (i in 0 until totalTimes) {
                for (j in i + 1 until totalTimes) {
                    partidas.add(
                        Partida(
                            id = 0,
                            torneioId = torneioId,
                            time1Id = times[i],
                            time2Id = times[j],
                            placarTime1 = 0,
                            placarTime2 = 0,
                            dataHora = formatoData.format(Date()),
                            fase = FaseTorneio.GRUPOS,
                            rodada = partidas.size + 1
                        )
                    )
                }
            }
            partidas
        }
    }
}
