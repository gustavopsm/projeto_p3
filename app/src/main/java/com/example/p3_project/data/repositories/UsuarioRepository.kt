package com.example.p3_project.data.repositories

import com.example.p3_project.data.dao.UsuarioDao
import com.example.p3_project.data.entities.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun registerUsuario(usuario: Usuario) {
        usuarioDao.insert(usuario)
    }

    suspend fun getUsuarioByEmail(email: String): Usuario? {
        return usuarioDao.getByEmail(email)
    }
}
