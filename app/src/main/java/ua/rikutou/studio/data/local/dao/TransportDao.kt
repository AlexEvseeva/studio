package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}