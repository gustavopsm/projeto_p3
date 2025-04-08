package com.example.p3_project.data.dao

import androidx.room.*
import com.example.p3_project.data.entities.Partida
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Query("SELECT * FROM partidas")
    fun getAllPartidas(): Flow<List<Partida>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(partida: Partida)

    @Update
    suspend fun update(partida: Partida)

    @Delete
    suspend fun delete(partida: Partida)

    @Query("SELECT * FROM partidas WHERE torneioId = :torneioId")
    fun getPartidasPorTorneio(torneioId: Long): Flow<List<Partida>>

    @Query("SELECT * FROM partidas WHERE id = :id")
    suspend fun getPartidaById(id: Long): Partida?
}
