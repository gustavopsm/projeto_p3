package com.example.p3_project

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.p3_project.databinding.ActivityMainBinding
import com.example.p3_project.viewmodels.TorneioViewModel
import com.example.p3_project.ui.viewmodel.TorneioViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import com.example.p3_project.data.entities.Torneio

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val torneioViewModel: TorneioViewModel by viewModels {
        TorneioViewModelFactory((application as MeuApp).repository)
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

        // Testando se os torneios estão sendo carregados
        lifecycleScope.launch {
            torneioViewModel.torneios.collectLatest { torneio ->
                println("Lista de torneios carregada: $torneio")
            }
        }
    }
}
