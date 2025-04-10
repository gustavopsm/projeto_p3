package com.example.p3_project

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.p3_project.data.AppDatabase
import com.example.p3_project.data.dao.TorneioDao
import com.example.p3_project.data.entities.Torneio
import com.example.p3_project.data.entities.TipoTorneio
import com.example.p3_project.data.entities.TipoEsporte
import com.example.p3_project.data.entities.StatusTorneio
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
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
            descricao = "Descrição antiga",
            tipo = TipoEsporte.ESPORTS,
            dataInicio = "2025-04-05",
            tipoTorneio = TipoTorneio.MATA_MATA,
            status = StatusTorneio.PLANEJADO
        )
        dao.insert(torneio)

        val torneios = dao.getAllTorneios().first()
        assertEquals(1, torneios.size)
        assertEquals("Copa Teste", torneios[0].nome)
        assertEquals("2025-04-05", torneios[0].dataInicio)
        assertEquals(TipoTorneio.MATA_MATA, torneios[0].tipoTorneio)
        assertEquals(TipoEsporte.ESPORTS, torneios[0].tipo)
        assertEquals(StatusTorneio.PLANEJADO, torneios[0].status)
    }

    @Test
    fun updateTorneio() = runBlocking {
        val torneio = Torneio(
            nome = "Copa Antiga",
            descricao = "Descrição antiga",
            tipo = TipoEsporte.FUTEBOL,
            dataInicio = "2025-04-01",
            dataFim = "2025-04-10",
            tipoTorneio = TipoTorneio.PONTOS_CORRIDOS,
            status = StatusTorneio.PLANEJADO
        )
        val id = dao.insert(torneio)

        val torneioAtualizado = Torneio(
            id = id,
            nome = "Copa Nova",
            descricao = "Nova descrição",
            tipo = TipoEsporte.FUTEBOL,
            dataInicio = "2025-04-05",
            dataFim = "2025-04-15",
            tipoTorneio = TipoTorneio.MISTO,
            status = StatusTorneio.EM_ANDAMENTO
        )
        dao.update(torneioAtualizado)

        val torneioBuscado = dao.getTorneioById(id)
        assertNotNull(torneioBuscado)
        assertEquals("Copa Nova", torneioBuscado?.nome)
        assertEquals("Nova descrição", torneioBuscado?.descricao)
        assertEquals("2025-04-05", torneioBuscado?.dataInicio)
        assertEquals("2025-04-15", torneioBuscado?.dataFim)
        assertEquals(TipoTorneio.MISTO, torneioBuscado?.tipoTorneio)
        assertEquals(StatusTorneio.EM_ANDAMENTO, torneioBuscado?.status)
    }

    @Test
    fun deleteTorneio() = runBlocking {
        val torneio = Torneio(
            nome = "Torneio a ser deletado",
            tipo = TipoEsporte.FUTEBOL,
            dataInicio = "2025-04-05",
            tipoTorneio = TipoTorneio.MATA_MATA,
            status = StatusTorneio.PLANEJADO
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
            tipo = TipoEsporte.FUTEBOL,
            dataInicio = "2025-04-05",
            tipoTorneio = TipoTorneio.PONTOS_CORRIDOS,
            status = StatusTorneio.EM_ANDAMENTO
        )
        val torneio2 = Torneio(
            nome = "Copa Local",
            tipo = TipoEsporte.ESPORTS,
            dataInicio = "2025-04-10",
            tipoTorneio = TipoTorneio.MATA_MATA,
            status = StatusTorneio.PLANEJADO
        )
        val torneio3 = Torneio(
            nome = "Copa Nacional",
            tipo = TipoEsporte.FUTEBOL,
            dataInicio = "2025-04-15",
            tipoTorneio = TipoTorneio.MISTO,
            status = StatusTorneio.EM_ANDAMENTO
        )

        dao.insert(torneio1)
        dao.insert(torneio2)
        dao.insert(torneio3)

        val torneiosEmAndamento = dao.getTorneiosByStatus(StatusTorneio.EM_ANDAMENTO).first()
        assertEquals(2, torneiosEmAndamento.size)
    }
}
