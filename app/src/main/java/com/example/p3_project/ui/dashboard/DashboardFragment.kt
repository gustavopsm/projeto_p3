package com.example.p3_project.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.data.dao.TorneioDao
import com.example.p3_project.data.repositories.TorneioRepository
import com.example.p3_project.data.repository.PartidaRepository
import com.example.p3_project.databinding.FragmentDashboardBinding
import com.example.p3_project.domain.TorneioManager
import com.example.p3_project.viewmodels.TorneioViewModel
import com.example.p3_project.viewmodel.TorneioViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var torneioViewModel: TorneioViewModel
    private lateinit var adapter: TorneioAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // ✅ Correção: Injeção manual das dependências do ViewModel
        val appDatabase = AppDatabase.getDatabase(requireContext())
        val torneioDao = appDatabase.torneioDao()
        val partidaDao = appDatabase.partidaDao()

        val partidaRepository = PartidaRepository(partidaDao)
        val torneioRepository = TorneioRepository(torneioDao)
        val torneioManager = TorneioManager(partidaRepository)

        torneioViewModel = ViewModelProvider(this, TorneioViewModelFactory(
            torneioRepository,
            torneioManager,
            partidaRepository
        )).get(TorneioViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeTorneios()
    }

    private fun setupRecyclerView() {
        adapter = TorneioAdapter(emptyList())
        binding.recyclerViewTorneios.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTorneios.adapter = adapter
    }

    private fun observeTorneios() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE

            torneioViewModel.torneios.collect { torneiosList ->
                binding.progressBar.visibility = View.GONE

                if (torneiosList.isEmpty()) {
                    binding.recyclerViewTorneios.visibility = View.GONE
                    binding.textViewEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewTorneios.visibility = View.VISIBLE
                    binding.textViewEmpty.visibility = View.GONE
                    adapter.updateList(torneiosList)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
