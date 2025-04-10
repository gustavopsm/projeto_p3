package com.example.p3_project.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.ColumnInfo

@Entity(
    tableName = "partidas",
    foreignKeys = [
        ForeignKey(
            entity = Torneio::class,
            parentColumns = ["id"],
            childColumns = ["torneioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Time::class,
            parentColumns = ["id"],
            childColumns = ["time1Id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Time::class,
            parentColumns = ["id"],
            childColumns = ["time2Id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["torneioId"]), Index(value = ["time1Id"]), Index(value = ["time2Id"])]
)
data class Partida(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "torneioId")
    val torneioId: Long,

    @ColumnInfo(name = "fase")
    val fase: FaseTorneio, // "Grupos" ou "Mata-Mata"

    @ColumnInfo(name = "rodada")
    val rodada: Int, // Ex: 1 = Oitavas, 2 = Quartas, etc.

    @ColumnInfo(name = "grupo")
    val grupo: String? = null, // Apenas para fase de grupos

    @ColumnInfo(name = "time1Id")
    val time1Id: Long,

    @ColumnInfo(name = "time2Id")
    val time2Id: Long,

    @ColumnInfo(name = "placarTime1")
    val placarTime1: Int = 0,

    @ColumnInfo(name = "placarTime2")
    val placarTime2: Int = 0,

    @ColumnInfo(name = "dataHora")
    val dataHora: String // Formato "yyyy-MM-dd HH:mm"
)
