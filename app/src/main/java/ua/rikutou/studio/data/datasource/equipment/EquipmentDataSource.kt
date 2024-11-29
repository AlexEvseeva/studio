package ua.rikutou.studio.data.datasource.equipment

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.EquipmentEntity

interface EquipmentDataSource {
    suspend fun getAllEquipment(studioId: Long): Flow<List<EquipmentEntity>>
    suspend fun getEquipmentById(studioId: Long, equipmentId: Long): Flow<EquipmentEntity>
    suspend fun deleteEquipmentById(equipmentId: Long)
    suspend fun updateEquipment(equipment: EquipmentEntity)
    suspend fun loadEquipment(studioId: Long, search: String? = null)
}