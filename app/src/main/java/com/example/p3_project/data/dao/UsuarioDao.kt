package com.example.p3_project.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p3_project.data.entities.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): Usuario?
}