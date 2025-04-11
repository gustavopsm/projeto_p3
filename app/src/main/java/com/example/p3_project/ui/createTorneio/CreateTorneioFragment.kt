package com.example.p3_project.ui.createTorneio

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.data.entities.StatusTorneio
import com.example.p3_project.data.entities.TipoEsporte
import com.example.p3_project.data.entities.TipoTorneio
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.repositories.TorneioRepository
import com.example.p3_project.data.repository.PartidaRepository
import com.example.p3_project.databinding.FragmentCreateTorneioBinding
import com.example.p3_project.domain.TorneioManager
import com.example.p3_project.viewmodels.TorneioViewModel
import com.example.p3_project.viewmodel.TorneioViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CreateTorneioFragment : Fragment() {

    private var _binding: FragmentCreateTorneioBinding? = null
    private val binding get() = _binding!!

    private lateinit var torneioViewModel: TorneioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTorneioBinding.inflate(inflater, container, false)

        // Inicializa ViewModel
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

        setupTipoTorneioSpinner()
        setupTipoEsporteSpinner()
        setupDatePicker()
        setupCreateButton()
    }

    private fun setupTipoTorneioSpinner() {
        val tiposTorneio = listOf("Mata-Mata", "Pontos Corridos")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tiposTorneio
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipoTorneio.adapter = adapter
    }

    private fun setupTipoEsporteSpinner() {
        val tiposEsporte = listOf("Futebol", "eSports")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tiposEsporte
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipoEsporte.adapter = adapter
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()

        binding.etDataInicio.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.etDataInicio.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupCreateButton() {
        binding.btnCriarTorneio.setOnClickListener {
            val nomeTorneio = binding.etNomeTorneio.text.toString()
            val descricao = binding.etDescricaoTorneio.text.toString()
            val numTimesText = binding.etNumTimesPorGrupo.text.toString()
            val dataInicio = binding.etDataInicio.text.toString()

            val tipoTorneioSelecionado = binding.spinnerTipoTorneio.selectedItem.toString()
            val tipoEsporteSelecionado = binding.spinnerTipoEsporte.selectedItem.toString()

            if (nomeTorneio.isBlank() || dataInicio.isBlank()) {
                Toast.makeText(requireContext(), "Preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val numTimes = numTimesText.toIntOrNull()

            val tipoTorneio = when (tipoTorneioSelecionado) {
                "Mata-Mata" -> TipoTorneio.MATA_MATA
                "Pontos Corridos" -> TipoTorneio.PONTOS_CORRIDOS
                else -> TipoTorneio.MATA_MATA
            }

            val tipoEsporte = when (tipoEsporteSelecionado) {
                "Futebol" -> TipoEsporte.FUTEBOL
                "eSports" -> TipoEsporte.ESPORTS
                else -> TipoEsporte.FUTEBOL
            }

            val torneio = Torneio(
                nome = nomeTorneio,
                descricao = descricao.ifBlank { null },
                tipo = tipoEsporte,
                tipoTorneio = tipoTorneio,
                numTimesPorGrupo = numTimes,
                dataInicio = dataInicio,
                dataFim = null,
                status = StatusTorneio.EM_ANDAMENTO
            )

            CoroutineScope(Dispatchers.IO).launch {
                torneioViewModel.insert(torneio)

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Torneio criado com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
