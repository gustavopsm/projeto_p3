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
    val tipo: TipoEsporte, // Ex: "Futebol", "eSports"

    @ColumnInfo(name = "tipo_torneio")
    val tipoTorneio: TipoTorneio,

    @ColumnInfo(name = "num_times_por_grupo")
    val numTimesPorGrupo: Int? = null, // Apenas para torneios mistos

    @ColumnInfo(name = "data_inicio")
    val dataInicio: String, // Formato: "yyyy-MM-dd"

    @ColumnInfo(name = "data_fim")
    val dataFim: String? = null,

    @ColumnInfo(name = "status")
    val status: StatusTorneio
)