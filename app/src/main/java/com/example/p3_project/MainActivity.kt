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
import com.example.p3_project.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import com.example.p3_project.viewmodels.TorneioViewModel
import com.example.p3_project.ui.viewmodel.TorneioViewModelFactory
import com.example.p3_project.viewmodels.TimeViewModel
import com.example.p3_project.viewmodels.TimeViewModelFactory
import com.example.p3_project.viewmodels.PartidaViewModel
import com.example.p3_project.viewmodels.PartidaViewModelFactory

import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.entities.Time
import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.repository.PartidaRepository
import com.example.p3_project.data.AppDatabase

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

        val torneioId = 1

        lifecycleScope.launch {
            torneioViewModel.torneios.collectLatest { torneios ->
                println("Lista de torneios carregada: $torneios")
            }
        }

        lifecycleScope.launch {
            val novoTime = Time(id = 0, nome = "Time Teste", torneioId = torneioId)
            timeViewModel.insertTime(novoTime)
            Log.d("TESTE_BANCO", "Time inserido no banco: ${novoTime.nome}")
        }

        lifecycleScope.launch {
            val torneioId: Long = 1L
            partidaViewModel.getPartidasPorTorneio(torneioId).collectLatest { partidas ->
                Log.d("TESTE_BANCO", "Partidas do Torneio $torneioId: $partidas")
            }
        }
    }
}
