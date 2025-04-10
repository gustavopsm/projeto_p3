package com.example.p3_project.domain

import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.repository.PartidaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PontuacaoManager(private val partidaRepository: PartidaRepository) {

    suspend fun processarResultado(partida: Partida, tipoTorneio: TipoTorneio, partidaIda: Partida? = null) {
        withContext(Dispatchers.IO) {
            when (tipoTorneio) {
                TipoTorneio.PONTOS_CORRIDOS -> atualizarPontuacao(partida)
                TipoTorneio.MATA_MATA -> definirClassificadoMataMata(partida, partidaIda)
            }
        }
    }

    private fun atualizarPontuacao(partida: Partida) {
        if (partida.placarTime1 > partida.placarTime2) {
            atualizarPontos(partida.time1Id.toInt(), 3)
            atualizarPontos(partida.time2Id.toInt(), 0)
        } else if (partida.placarTime1 < partida.placarTime2) {
            atualizarPontos(partida.time1Id.toInt(), 0)
            atualizarPontos(partida.time2Id.toInt(), 3)
        } else {
            atualizarPontos(partida.time1Id.toInt(), 1)
            atualizarPontos(partida.time2Id.toInt(), 1)
        }
    }


    private fun definirClassificadoMataMata(partidaVolta: Partida, partidaIda: Partida?) {
        if (partidaIda == null) {
            // Jogo único - passa quem venceu
            val classificado = when {
                partidaVolta.placarTime1 > partidaVolta.placarTime2 -> partidaVolta.time1Id
                partidaVolta.placarTime1 < partidaVolta.placarTime2 -> partidaVolta.time2Id
                else -> null // Empate, pode precisar de prorrogação/pênaltis
            }
            println("Classificado para a próxima fase: Time $classificado")
        } else {
            // Ida e Volta - saldo de gols decide
            val saldoTime1 = (partidaIda.placarTime1 + partidaVolta.placarTime1) - (partidaIda.placarTime2 + partidaVolta.placarTime2)
            val classificado = when {
                saldoTime1 > 0 -> partidaVolta.time1Id
                saldoTime1 < 0 -> partidaVolta.time2Id
                else -> null // Empate no agregado, pode precisar de critério de desempate
            }
            println("Classificado no agregado: Time $classificado")
        }
    }

    private fun atualizarPontos(timeId: Int, pontos: Int) {
        println("Time $timeId recebeu $pontos pontos") // Simulação para futura integração com ranking
    }
}

enum class TipoTorneio {
    PONTOS_CORRIDOS, MATA_MATA
}
