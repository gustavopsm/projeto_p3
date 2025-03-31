package com.example.p3_project.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import com.example.p3_project.data.entities.Torneio

@Dao
interface TorneioDao {
    @Query("SELECT * FROM torneios")
    fun getAllTorneios(): Flow<List<Torneio>>

    @Insert
    suspend fun insert(torneio: Torneio): Long

    @Update
    suspend fun update(torneio: Torneio)

    @Delete
    suspend fun delete(torneio: Torneio)

    @Query("SELECT * FROM torneios WHERE status = :status")
    fun getTorneiosByStatus(status: String): Flow<List<Torneio>>

    @Query("SELECT * FROM torneios WHERE id = :id LIMIT 1")
    suspend fun getTorneioById(id: Long): Torneio?
}
