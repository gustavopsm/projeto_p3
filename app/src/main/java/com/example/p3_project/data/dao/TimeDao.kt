package com.example.p3_project.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.p3_project.data.entities.Time

@Dao
interface TimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(time: Time)

    @Update
    suspend fun update(time: Time)

    @Delete
    suspend fun delete(time: Time)

    @Query("SELECT * FROM times WHERE torneioId = :torneioId")
    fun getTimesPorTorneio(torneioId: Int): Flow<List<Time>>

    @Query("SELECT * FROM times WHERE id = :id")
    suspend fun getTimeById(id: Int): Time?

    @Query("SELECT * FROM times")
    fun getAllTimes(): Flow<List<Time>>
}
