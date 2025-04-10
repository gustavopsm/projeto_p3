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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.collectLatest
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

import com.example.p3_project.data.entities.*
import com.example.p3_project.model.LoginRequest
import com.example.p3_project.security.CriptografiaUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val torneioViewModel: TorneioViewModel by viewModels {
        val app = application as MeuApp
        TorneioViewModelFactory(app.torneioRepository, app.torneioManager, app.partidaRepository)
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

        lifecycleScope.launch(Dispatchers.IO) {
            criarTorneioSeNecessario(torneioId)
            delay(500) // Aguarda persistência
            criarTimesSeNecessario(torneioId)
            delay(500) // Aguarda persistência
            criarPartidaSeNecessario(torneioId)
        }

        monitorarBancoDeDados(torneioId)

        testarAutenticacao()
    }

    private suspend fun criarTorneioSeNecessario(torneioId: Long) {
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

    private suspend fun criarTimesSeNecessario(torneioId: Long) {
        val timesList = timeViewModel.times.firstOrNull() ?: emptyList()
        if (timesList.isEmpty()) {
            Log.d("TESTE_BANCO", "Nenhum time encontrado, inserindo novos times.")

            val time1 = Time(id = 1L, nome = "Time 1", torneioId = torneioId)
            val time2 = Time(id = 2L, nome = "Time 2", torneioId = torneioId)

            timeViewModel.insertTime(time1)
            timeViewModel.insertTime(time2)

            Log.d("TESTE_BANCO", "Times inseridos: ${time1.nome} e ${time2.nome}")
        } else {
            Log.d("TESTE_BANCO", "Times já existem no banco: $timesList")
        }

        val timesConfirmados = timeViewModel.times.firstOrNull() ?: emptyList()
        if (timesConfirmados.size < 2) {
            Log.e("TESTE_BANCO", "Erro: Times não persistidos corretamente.")
            Log.e("TESTE_BANCO", "Lista atual: $timesConfirmados")
        }
    }

    private suspend fun criarPartidaSeNecessario(torneioId: Long) {
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
            fase = FaseTorneio.GRUPOS,
            rodada = 1
        )

        partidaViewModel.insert(novaPartida)
        Log.d("TESTE_BANCO", "Nova partida adicionada: $novaPartida")
    }

    private fun monitorarBancoDeDados(torneioId: Long) {
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
    }

    private fun testarAutenticacao() {
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
