package com.example.p3_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.p3_project.data.repositories.UsuarioRepository
import com.example.p3_project.security.CriptografiaUtil
import com.example.p3_project.security.JwtUtil
import com.example.p3_project.model.LoginRequest
import com.example.p3_project.model.LoginResponse
import com.example.p3_project.data.entities.Usuario
import kotlinx.coroutines.launch

class AuthViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    fun registerUsuario(nome: String, email: String, senha: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val usuarioExistente = usuarioRepository.getUsuarioByEmail(email)
                if (usuarioExistente == null) {
                    val senhaHash = CriptografiaUtil.hashSenha(senha)
                    val usuario = Usuario(id = 0, nome = nome, email = email, senhaHash = senhaHash)

                    usuarioRepository.registerUsuario(usuario)
                    callback(true)
                } else {
                    callback(false) // Usuário já existe
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(false)
            }
        }
    }

    fun login(loginRequest: LoginRequest, callback: (LoginResponse?) -> Unit) {
        viewModelScope.launch {
            try {
                val usuario = usuarioRepository.getUsuarioByEmail(loginRequest.email)
                if (usuario != null && CriptografiaUtil.verificarSenha(loginRequest.senha, usuario.senhaHash)) {
                    val token = JwtUtil.generateToken(usuario.email)
                    callback(LoginResponse(token))
                } else {
                    callback(null) // Credenciais inválidas
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }

    // Adicionando a Factory para criação do ViewModel
    class Factory(private val usuarioRepository: UsuarioRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(usuarioRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
