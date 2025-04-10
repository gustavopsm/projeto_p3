package com.example.p3_project.domain

import com.example.p3_project.data.entities.Partida

object TorneioManager {

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
