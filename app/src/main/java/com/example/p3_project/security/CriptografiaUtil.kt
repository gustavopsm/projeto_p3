package com.example.p3_project.security

import at.favre.lib.crypto.bcrypt.BCrypt

object CriptografiaUtil {
    fun hashSenha(senha: String): String {
        return BCrypt.withDefaults().hashToString(12, senha.toCharArray())
    }

    fun verificarSenha(senha: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(senha.toCharArray(), hash).verified
    }
}