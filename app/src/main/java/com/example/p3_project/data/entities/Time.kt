package com.example.p3_project.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "times")
data class Time(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val torneioId: Int
)
