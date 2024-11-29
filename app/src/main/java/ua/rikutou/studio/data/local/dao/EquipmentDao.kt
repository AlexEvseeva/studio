package ua.rikutou.studio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.EquipmentEntity

@Dao
interface EquipmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<EquipmentEntity>): List<Long>

    @Query("SELECT * FROM equipmententity WHERE studioId=:studioId")
    fun getAllEquipment(studioId: Long): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipmententity WHERE studioId=:studioId AND equipmentId=:equipmentId")
    fun getEquipmentById(studioId: Long, equipmentId: Long): Flow<EquipmentEntity>

    @Query("DELETE FROM equipmententity WHERE equipmentId=:equipmentId")
    suspend fun deleteById(equipmentId: Long)
}