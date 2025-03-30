import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface `TorneioDao` {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(torneio: `Torneio`): Long

    @Update
    suspend fun update(torneio: `Torneio`)

    @Delete
    suspend fun delete(torneio: `Torneio`)

    @Query("SELECT * FROM torneios ORDER BY data_inicio DESC")
    fun getAllTorneios(): Flow<List<`Torneio`>>

    @Query("SELECT * FROM torneios WHERE id = :id")
    suspend fun getTorneioById(id: Long): `Torneio`?

    @Query("SELECT * FROM torneios WHERE status = :status")
    fun getTorneiosByStatus(status: String): Flow<List<`Torneio`>>
}