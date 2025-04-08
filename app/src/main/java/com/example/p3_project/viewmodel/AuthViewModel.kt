package com.example.p3_project.viewmodel

import androidx.lifecycle.ViewModel
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
            val usuarioExistente = usuarioRepository.getUsuarioByEmail(email)
            if (usuarioExistente == null) {
                val usuario = Usuario(id = 0, nome = nome, email = email, senhaHash = CriptografiaUtil.hashSenha(senha))
                usuarioRepository.registerUsuario(usuario)
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun login(loginRequest: LoginRequest, callback: (LoginResponse?) -> Unit) {
        viewModelScope.launch {
            val usuario = usuarioRepository.getUsuarioByEmail(loginRequest.email)
            if (usuario != null && CriptografiaUtil.verificarSenha(loginRequest.senha, usuario.senhaHash)) {
                val token = JwtUtil.generateToken(usuario.email)
                callback(LoginResponse(token))
            } else {
                callback(null)
            }
        }
    }
}
