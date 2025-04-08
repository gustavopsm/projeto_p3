package com.example.p3_project.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*

object JwtUtil {
    private const val SECRET_KEY = "minha_chave_super_secreta" // 🔒 Recomenda-se armazenar em variáveis de ambiente
    private const val EXPIRATION_TIME = 86_400_000 // 24 horas em milissegundos

    // Gera um Token JWT
    fun generateToken(username: String): String {
        return JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(Algorithm.HMAC256(SECRET_KEY))
    }

    // Valida um Token JWT
    fun validateToken(token: String): Boolean {
        return try {
            val verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
            verifier.verify(token)
            true
        } catch (exception: JWTVerificationException) {
            false
        }
    }

    // Obtém o nome de usuário do Token
    fun getUsernameFromToken(token: String): String? {
        return try {
            val decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
            decodedJWT.subject
        } catch (exception: JWTVerificationException) {
            null
        }
    }
}
