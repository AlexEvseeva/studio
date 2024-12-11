package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.TransportSelectionEntity

@Dao
interface TransportSelectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TransportSelectionEntity)

    @Query("DELETE FROM transportselectionentity WHERE transportId IN (:transportIds)")
    suspend fun delete(transportIds: List<Long>)

    @Query("SELECT transportId FROM TransportSelectionEntity")
    fun getAll(): Flow<List<Long>>

    @Query("DELETE FROM transportselectionentity")
    suspend fun clearSelections()
}