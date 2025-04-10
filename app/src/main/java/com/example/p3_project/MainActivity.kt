package com.example.p3_project

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

import com.example.p3_project.databinding.ActivityMainBinding
import com.example.p3_project.viewmodel.AuthViewModel
import com.example.p3_project.viewmodels.TorneioViewModel
import com.example.p3_project.viewmodel.TorneioViewModelFactory
import com.example.p3_project.viewmodels.TimeViewModel
import com.example.p3_project.viewmodels.TimeViewModelFactory
import com.example.p3_project.viewmodels.PartidaViewModel
import com.example.p3_project.viewmodels.PartidaViewModelFactory

import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.entities.*
import com.example.p3_project.data.repository.PartidaRepository
import com.example.p3_project.data.repositories.UsuarioRepository


import com.example.p3_project.security.CriptografiaUtil
import com.example.p3_project.model.LoginRequest
import kotlinx.coroutines.flow.firstOrNull

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val torneioViewModel: TorneioViewModel by viewModels {
        TorneioViewModelFactory((application as MeuApp).torneioRepository)
    }

    private val timeViewModel: TimeViewModel by viewModels {
        TimeViewModelFactory((application as MeuApp).timeRepository)
    }

    private val partidaViewModel: PartidaViewModel by viewModels {
        PartidaViewModelFactory((application as MeuApp).partidaRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val torneioId = 1L

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val torneiosList = torneioViewModel.torneios.firstOrNull() ?: emptyList()

                if (torneiosList.isEmpty()) {
                    val novoTorneio = Torneio(
                        id = torneioId,
                        nome = "Torneio Teste",
                        descricao = "Descrição do torneio",
                        tipo = TipoEsporte.FUTEBOL,
                        dataInicio = "2025-04-05",
                        tipoTorneio = TipoTorneio.MATA_MATA,
                        status = StatusTorneio.PLANEJADO
                    )
                    torneioViewModel.insert(novoTorneio)
                    Log.d("TESTE_BANCO", "Torneio inserido: ${novoTorneio.nome}")
                }
            }

            val torneiosConfirmados = torneioViewModel.torneios.firstOrNull() ?: emptyList()
            if (torneiosConfirmados.isEmpty()) {
                Log.e("TESTE_BANCO", "Erro: Torneio não persistido no banco")
                return@launch
            }

            // Criar times caso ainda não existam
            withContext(Dispatchers.IO) {
                val timesList = timeViewModel.times.firstOrNull() ?: emptyList()
                if (timesList.isEmpty()) {
                    val time1 = Time(id = 1L, nome = "Time 1", torneioId = torneioId)
                    val time2 = Time(id = 2L, nome = "Time 2", torneioId = torneioId)
                    timeViewModel.insertTime(time1)
                    timeViewModel.insertTime(time2)
                    Log.d("TESTE_BANCO", "Times inseridos: ${time1.nome} e ${time2.nome}")
                }
            }

            // Espera os times serem persistidos
            val timesConfirmados = timeViewModel.times.firstOrNull() ?: emptyList()
            if (timesConfirmados.size < 2) {
                Log.e("TESTE_BANCO", "Erro: Times não persistidos no banco")
                return@launch
            }

            // Criar uma partida depois de garantir que torneio e times existem
            withContext(Dispatchers.IO) {
                val formatoData = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val dataFormatada = formatoData.format(Date(System.currentTimeMillis()))

                val novaPartida = Partida(
                    id = 0,
                    torneioId = torneioId,
                    time1Id = 1L,
                    time2Id = 2L,
                    placarTime1 = 0,
                    placarTime2 = 0,
                    dataHora = dataFormatada,
                    fase = "Grupo A",
                    rodada = 1
                )

                partidaViewModel.insert(novaPartida)
                Log.d("TESTE_BANCO", "Nova partida adicionada: $novaPartida")
            }
        }

        lifecycleScope.launch {
            torneioViewModel.torneios.collectLatest { torneios ->
                Log.d("TESTE_BANCO", "Lista de torneios carregada: $torneios")
            }
        }

        lifecycleScope.launch {
            partidaViewModel.getPartidasPorTorneio(torneioId).collectLatest { partidas ->
                Log.d("TESTE_BANCO", "Partidas do Torneio $torneioId: $partidas")
            }
        }

        // Teste de autenticação
        lifecycleScope.launch {
            try {
                val authViewModel = AuthViewModel((application as MeuApp).usuarioRepository)

                authViewModel.registerUsuario("Usuário Teste", "teste@email.com", "senha123") { sucesso ->
                    if (sucesso) {
                        Log.d("AuthTest", "Usuário cadastrado com sucesso!")
                    } else {
                        Log.e("AuthTest", "Erro: Usuário já existe no banco.")
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthTest", "Erro ao cadastrar usuário", e)
            }
        }

        val usuarioRepository = (application as MeuApp).usuarioRepository
        val authViewModel = AuthViewModel(usuarioRepository)

        val emailTeste = "teste@email.com"
        val senhaTeste = "123456"

        authViewModel.login(LoginRequest(emailTeste, senhaTeste)) { response ->
            if (response != null) {
                Log.d("AuthTest", "Login realizado com sucesso! Token: ${response.token}")
            } else {
                Log.d("AuthTest", "Falha no login: Credenciais inválidas")
            }
        }

        val senha = "minhaSenhaSegura123"
        val senhaHash = CriptografiaUtil.hashSenha(senha)
        val verificacao = CriptografiaUtil.verificarSenha(senha, senhaHash)

        Log.d("CriptografiaTeste", "Senha original: $senha")
        Log.d("CriptografiaTeste", "Hash gerado: $senhaHash")
        Log.d("CriptografiaTeste", "Senha confere? $verificacao")
    }
}
