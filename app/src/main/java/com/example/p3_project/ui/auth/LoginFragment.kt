package com.example.p3_project.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.p3_project.R
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.model.LoginRequest
import com.example.p3_project.security.JwtUtil
import com.example.p3_project.viewmodel.AuthViewModel
import com.example.p3_project.viewmodel.AuthViewModelFactory

class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AppDatabase.getDatabase(requireContext()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se o usuário já está autenticado
        val token = JwtUtil.loadToken(requireContext())
        if (token != null && JwtUtil.isTokenValid(token)) {
            findNavController().navigate(R.id.action_login_to_navigation_dashboard)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etSenha = view.findViewById<EditText>(R.id.etSenha)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        authViewModel.registerUsuario(
            nome = "Usuário Teste",
            email = "teste@teste.com",
            senha = "123456"
        ) { sucesso ->
            if (sucesso) {
                android.util.Log.d("SEED", "Usuário de teste criado com sucesso!")
            } else {
                android.util.Log.d("SEED", "Usuário de teste já existe ou erro na criação.")
            }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                authViewModel.login(LoginRequest(email, senha)) { response ->
                    if (response != null) {
                        JwtUtil.saveToken(requireContext(), response.token)
                        Toast.makeText(requireContext(), "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.navigation_dashboard)
                    } else {
                        Toast.makeText(requireContext(), "Erro no login. Verifique suas credenciais!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
