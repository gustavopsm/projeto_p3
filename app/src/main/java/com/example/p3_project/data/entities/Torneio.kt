package com.example.p3_project.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "torneios")
data class Torneio(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "descricao")
    val descricao: String? = null,

    @ColumnInfo(name = "tipo")
    val tipo: String, // Ex: "Futebol", "eSports"

    @ColumnInfo(name = "data_inicio")
    val dataInicio: String, // Formato: "yyyy-MM-dd"

    @ColumnInfo(name = "data_fim")
    val dataFim: String? = null,

    @ColumnInfo(name = "status")
    val status: String = "Planejado" // "Planejado", "Em Andamento", "Cancelado", "Concluído"
)