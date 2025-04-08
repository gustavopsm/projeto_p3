package com.example.p3_project.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "partidas",
    foreignKeys = [
        ForeignKey(
            entity = Torneio::class,
            parentColumns = ["id"],
            childColumns = ["torneioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["torneioId"])]
)
data class Partida(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val torneioId: Long,
    val time1Id: Int,
    val time2Id: Int,
    val placarTime1: Int = 0,
    val placarTime2: Int = 0,
    val dataHora: String
)