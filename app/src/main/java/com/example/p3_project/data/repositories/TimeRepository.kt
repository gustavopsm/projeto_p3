package com.example.p3_project.data.repository

import com.example.p3_project.data.dao.TimeDao
import com.example.p3_project.data.entities.Time
import kotlinx.coroutines.flow.Flow

class TimeRepository(private val timeDao: TimeDao) {

    val todosTimes: Flow<List<Time>> = timeDao.getTimesPorTorneio(0)


    suspend fun insertTime(time: Time) {
        timeDao.insert(time)
    }

    suspend fun updateTime(time: Time) {
        timeDao.update(time)
    }

    suspend fun deleteTime(time: Time) {
        timeDao.delete(time)
    }

    fun getTimesPorTorneio(torneioId: Int): Flow<List<Time>> {
        return timeDao.getTimesPorTorneio(torneioId)
    }

    suspend fun getTimeById(id: Int): Time? {
        return timeDao.getTimeById(id)
    }
}
