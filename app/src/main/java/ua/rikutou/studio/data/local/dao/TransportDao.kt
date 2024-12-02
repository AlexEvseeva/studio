package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.TransportEntity

@Dao
interface TransportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<TransportEntity>): List<Long>

    suspend fun syncInsert(list: List<TransportEntity>) {
        val ids = insert(list)
        deleteExcluded(ids)
    }

    @Query("DELETE FROM transportentity WHERE transportId NOT IN (:list)")
    suspend fun deleteExcluded(list: List<Long>)

    @Query("SELECT * FROM transportentity")
    fun getAll(): Flow<List<TransportEntity>>

    @Query("SELECT * FROM transportentity WHERE transportId=:transportId")
    fun getById(transportId: Long): Flow<TransportEntity>

    @Query("DELETE FROM transportentity WHERE transportId=:transportId")
    suspend fun deleteById(transportId: Long)
}