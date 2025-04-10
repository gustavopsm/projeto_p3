package com.example.p3_project.data

import androidx.room.TypeConverter
import com.example.p3_project.data.entities.TipoTorneio
import com.example.p3_project.data.entities.StatusTorneio

class Converters {
    @TypeConverter
    fun fromTipoTorneio(tipo: TipoTorneio): String {
        return tipo.name
    }

    @TypeConverter
    fun toTipoTorneio(tipo: String): TipoTorneio {
        return TipoTorneio.valueOf(tipo)
    }

    @TypeConverter
    fun fromStatusTorneio(status: StatusTorneio): String {
        return status.name
    }

    @TypeConverter
    fun toStatusTorneio(status: String): StatusTorneio {
        return StatusTorneio.valueOf(status)
    }
}
