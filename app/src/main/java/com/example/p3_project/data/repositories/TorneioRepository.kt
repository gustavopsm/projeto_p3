import kotlinx.coroutines.flow.Flow

class TorneioRepository(private val torneioDao: TorneioDao) {
    fun getAllTorneios(): Flow<List<Torneio>> = torneioDao.getAllTorneios()

    suspend fun insert(torneio: Torneio): Long {
        return torneioDao.insert(torneio)
    }

    suspend fun update(torneio: Torneio) {
        torneioDao.update(torneio)
    }

    suspend fun delete(torneio: Torneio) {
        torneioDao.delete(torneio)
    }

    fun getTorneiosByStatus(status: String): Flow<List<Torneio>> {
        return torneioDao.getTorneiosByStatus(status)
    }

    suspend fun getTorneio(id: Long): Torneio? {
        return torneioDao.getTorneioById(id)
    }
}