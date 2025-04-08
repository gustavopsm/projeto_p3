package com.example.p3_project

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.data.dao.TorneioDao
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.dao.PartidaDao
import com.example.p3_project.data.entities.Partida
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: TorneioDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.torneioDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndRetrieveTorneio() = runBlocking {
        val torneio = Torneio(
            id = 1,
            nome = "Copa Teste",
            dataInicio = "2025-04-05",
            tipo = "Eliminatório"
        )
        dao.insert(torneio)

        val torneios = dao.getAllTorneios().first()
        assertEquals(1, torneios.size)
        assertEquals("Copa Teste", torneios[0].nome)
        assertEquals("2025-04-05", torneios[0].dataInicio)
        assertEquals("Eliminatório", torneios[0].tipo)
    }

    @Test
    fun updateTorneio() = runBlocking {
        val torneio = Torneio(
            nome = "Copa Antiga",
            descricao = "Descrição antiga",
            tipo = "eSports",
            dataInicio = "2025-04-01",
            dataFim = "2025-04-10",
            status = "Planejado"
        )
        val id = dao.insert(torneio)

        val torneioAtualizado = Torneio(
            id = id,
            nome = "Copa Nova",
            descricao = "Nova descrição",
            tipo = "eSports",
            dataInicio = "2025-04-05",
            dataFim = "2025-04-15",
            status = "Em Andamento"
        )
        dao.update(torneioAtualizado)

        val torneioBuscado = dao.getTorneioById(id)
        assertNotNull(torneioBuscado)
        assertEquals("Copa Nova", torneioBuscado?.nome)
        assertEquals("Nova descrição", torneioBuscado?.descricao)
        assertEquals("2025-04-05", torneioBuscado?.dataInicio)
        assertEquals("2025-04-15", torneioBuscado?.dataFim)
        assertEquals("Em Andamento", torneioBuscado?.status)
    }

    @Test
    fun deleteTorneio() = runBlocking {
        val torneio = Torneio(
            nome = "Torneio a ser deletado",
            tipo = "Futebol",
            dataInicio = "2025-04-05",
            status = "Planejado"
        )
        val id = dao.insert(torneio)

        val torneioBuscado = dao.getTorneioById(id)
        assertNotNull(torneioBuscado)

        dao.delete(torneioBuscado!!)
        val torneioDeletado = dao.getTorneioById(id)
        assertNull(torneioDeletado)
    }

    @Test
    fun getTorneiosByStatus() = runBlocking {
        val torneio1 = Torneio(
            nome = "Copa Regional",
            tipo = "Futebol",
            dataInicio = "2025-04-05",
            status = "Em Andamento"
        )
        val torneio2 = Torneio(
            nome = "Copa Local",
            tipo = "eSports",
            dataInicio = "2025-04-10",
            status = "Planejado"
        )
        val torneio3 = Torneio(
            nome = "Copa Nacional",
            tipo = "Futebol",
            dataInicio = "2025-04-15",
            status = "Em Andamento"
        )

        dao.insert(torneio1)
        dao.insert(torneio2)
        dao.insert(torneio3)

        val torneiosEmAndamento = dao.getTorneiosByStatus("Em Andamento").first()
        assertEquals(2, torneiosEmAndamento.size)
    }

//    @Test
//    fun testInsertTime() = runBlocking {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        val db = AppDatabase.getDatabase(context)
//        val timeDao = db.timeDao()
//
//        val novoTime = Time(id = 1, nome = "Time A")
//
//        timeDao.insert(novoTime)
//
//        val times = timeDao.getAll()
//        assertTrue(times.contains(novoTime))
//    }


//    @Test
//    fun testInsertPartida() = runBlocking {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
//            .allowMainThreadQueries()
//            .build()
//
//        val partidaDao = db.partidaDao()
//
//        val novaPartida = Partida(
//            id = 1,
//            nome = "Final do Torneio",
//            torneioId = 1,
//            time1Id = 1,
//            time2Id = 2,
//            placarTime1 = 0,
//            placarTime2 = 0,
//            dataHora = "2025-04-07 15:30"
//        )
//
//        partidaDao.insert(novaPartida)
//
//        val partidas = partidaDao.getAllPartidas()
//        assertTrue(partidas.contains(novaPartida))
//
//        db.close()
//    }
}
