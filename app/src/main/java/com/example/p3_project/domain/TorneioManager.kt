package com.example.p3_project.domain

import com.example.p3_project.data.entities.FaseTorneio
import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.repository.PartidaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TorneioManager(private val partidaRepository: PartidaRepository) {
    private fun gerarDataHora(rodada: Int): String {
        val diasDepois = rodada * 2 // Cada rodada ocorre a cada 2 dias
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val data = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, diasDepois) }.time
        return formato.format(data)
    }
    /**
     * Sorteia as partidas do torneio conforme o tipo de torneio.
     */
    suspend fun sortearPartidas(torneioId: Long, times: List<Time>, tipoTorneio: TipoTorneio) {
        withContext(Dispatchers.IO) {
            val partidas = mutableListOf<Partida>()

            when (tipoTorneio) {
                TipoTorneio.PONTOS_CORRIDOS -> {
                    // Todos jogam contra todos
                    var rodadaAtual = 1
                    for (i in times.indices) {
                        for (j in i + 1 until times.size) {
                            partidas.add(
                                Partida(
                                    torneioId = torneioId,
                                    fase = FaseTorneio.GRUPOS,
                                    rodada = rodadaAtual++,
                                    grupo = null,
                                    time1Id = times[i].id,
                                    time2Id = times[j].id,
                                    placarTime1 = 0,
                                    placarTime2 = 0,
                                    dataHora = gerarDataHora(rodadaAtual)
                                )
                            )
                        }
                    }
                }

                TipoTorneio.MATA_MATA -> {
                    // Sorteia confrontos aleatórios
                    val shuffledTimes = times.shuffled()
                    var rodadaAtual = 1

                    for (i in shuffledTimes.indices step 2) {
                        if (i + 1 < shuffledTimes.size) {
                            partidas.add(
                                Partida(
                                    torneioId = torneioId,
                                    fase = FaseTorneio.MATA_MATA,
                                    rodada = rodadaAtual,
                                    grupo = null,
                                    time1Id = shuffledTimes[i].id,
                                    time2Id = shuffledTimes[i + 1].id,
                                    placarTime1 = 0,
                                    placarTime2 = 0,
                                    dataHora = gerarDataHora(rodadaAtual)
                                )
                            )
                            rodadaAtual++
                        }
                    }
                }
            }

            // Salvar as partidas no banco de dados
            partidas.forEach { partidaRepository.insert(it) }
        }
    }

    /**
     * Determina os classificados da fase de grupos.
     */
    fun classificarFaseDeGrupos(partidas: List<Partida>, numClassificados: Int): List<Long> {
        val tabela = mutableMapOf<Long, Pair<Int, Int>>() // timeId -> (pontos, saldo de gols)

        partidas.forEach { partida ->
            val resultadoTime1 = calcularPontuacao(partida.placarTime1, partida.placarTime2)
            val resultadoTime2 = calcularPontuacao(partida.placarTime2, partida.placarTime1)

            tabela[partida.time1Id] = tabela.getOrDefault(partida.time1Id, Pair(0, 0)).let { (pontos, saldo) ->
                Pair(pontos + resultadoTime1.first, saldo + resultadoTime1.second)
            }

            tabela[partida.time2Id] = tabela.getOrDefault(partida.time2Id, Pair(0, 0)).let { (pontos, saldo) ->
                Pair(pontos + resultadoTime2.first, saldo + resultadoTime2.second)
            }
        }

        // Ordena por pontos e depois por saldo de gols
        return tabela.entries
            .sortedWith(compareByDescending<Map.Entry<Long, Pair<Int, Int>>> { it.value.first }
                .thenByDescending { it.value.second })
            .map { it.key }
            .take(numClassificados)
    }

    /**
     * Define quem avança no mata-mata.
     */
    fun classificarMataMata(partidas: List<Partida>): Long {
        if (partidas.size == 1) { // Jogo único
            val partida = partidas[0]
            return if (partida.placarTime1 > partida.placarTime2) partida.time1Id else partida.time2Id
        }

        // Ida e volta
        val totalGols = mutableMapOf<Long, Int>()

        partidas.forEach { partida ->
            totalGols[partida.time1Id] = totalGols.getOrDefault(partida.time1Id, 0) + partida.placarTime1
            totalGols[partida.time2Id] = totalGols.getOrDefault(partida.time2Id, 0) + partida.placarTime2
        }

        val time1Id = partidas[0].time1Id
        val time2Id = partidas[0].time2Id

        return if ((totalGols[time1Id] ?: 0) > (totalGols[time2Id] ?: 0)) {
            time1Id
        } else {
            time2Id
        }
    }

    /**
     * Gera confrontos da fase mata-mata.
     */
    fun gerarConfrontosMataMata(classificados: List<Long>): List<Pair<Long, Long>> {
        val confrontos = mutableListOf<Pair<Long, Long>>()
        for (i in classificados.indices step 2) {
            if (i + 1 < classificados.size) {
                confrontos.add(Pair(classificados[i], classificados[i + 1]))
            }
        }
        return confrontos
    }

    /**
     * Calcula pontuação de um time em uma partida:
     * - Vitória: 3 pontos
     * - Empate: 1 ponto
     * - Derrota: 0 pontos
     */
    private fun calcularPontuacao(golsFeitos: Int, golsSofridos: Int): Pair<Int, Int> {
        return when {
            golsFeitos > golsSofridos -> Pair(3, golsFeitos - golsSofridos)
            golsFeitos == golsSofridos -> Pair(1, 0)
            else -> Pair(0, golsFeitos - golsSofridos)
        }
    }
}
