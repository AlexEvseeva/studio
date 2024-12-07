package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.EquipmentSelectionEntity

@Dao
interface EquipmentSelectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: EquipmentSelectionEntity)

    @Query("SELECT equipmentId FROM EquipmentSelectionEntity")
    fun getAll(): Flow<List<Long>>

    @Query("DELETE FROM equipmentselectionentity WHERE equipmentId IN (:list)")
    suspend fun delete(list: List<Long>)

}