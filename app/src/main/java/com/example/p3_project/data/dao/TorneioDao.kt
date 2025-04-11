package com.example.p3_project.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.entities.Partida
import com.example.p3_project.data.entities.StatusTorneio
import com.example.p3_project.data.entities.Time

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
    fun getTorneiosByStatus(status: StatusTorneio): Flow<List<Torneio>>

    @Query("SELECT * FROM torneios WHERE id = :id LIMIT 1")
    suspend fun getTorneioById(id: Long): Torneio?

    @Query("SELECT * FROM torneios WHERE tipo = :tipo")
    fun getTorneiosByTipo(tipo: String): Flow<List<Torneio>>

    @Query("UPDATE torneios SET status = :novoStatus WHERE id = :torneioId")
    suspend fun UpdateTorneioStatus(torneioId: Long, novoStatus: String)

    @Query("SELECT * FROM partidas WHERE torneioId = :torneioId ORDER BY rodada DESC LIMIT :quantidade")
    suspend fun getTimesQueAvancam(torneioId: Long, quantidade: Int): List<Partida>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(torneios: List<Torneio>)

    @Query("DELETE FROM torneios")
    suspend fun deleteAll()

}
