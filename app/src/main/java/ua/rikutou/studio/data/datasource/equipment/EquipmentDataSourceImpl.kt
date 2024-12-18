@file:OptIn(ExperimentalCoroutinesApi::class)

package ua.rikutou.studio.data.datasource.equipment

import android.util.Log
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.local.entity.EquipmentSelectionEntity
import ua.rikutou.studio.data.local.entity.toDto
import ua.rikutou.studio.data.remote.equipment.EquipmentApi
import ua.rikutou.studio.data.remote.equipment.EquipmentType
import ua.rikutou.studio.data.remote.equipment.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class EquipmentDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val equipmentApi: EquipmentApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : EquipmentDataSource {
    private val TAG by lazy { EquipmentDataSourceImpl::class.simpleName }

    override suspend fun getAllEquipment(studioId: Long): Flow<List<EquipmentEntity>> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.equipmentDao.getAllEquipment(studioId = studioId)
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun getEquipmentById(
        studioId: Long,
        equipmentId: Long
    ): Flow<EquipmentEntity> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.equipmentDao.getEquipmentById(
                    studioId = studioId,
                    equipmentId = equipmentId
                )
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun deleteEquipmentById(equipmentId: Long): Unit = withContext(Dispatchers.IO) {
        equipmentApi.deleteEquipment(
            equipmentId = equipmentId
        ).run {
            dbDataSource.db.equipmentDao.deleteById(equipmentId = equipmentId)
            when {
                isSuccessful -> {}
                else -> {
                    Log.e(TAG, "deleteEquipmentById: Error: equipmentId: $equipmentId")
                }
            }
        }
    }

    override suspend fun updateEquipment(equipment: EquipmentEntity): Unit = withContext(Dispatchers.IO) {
        equipmentApi.saveUpdateEquipment(
            body = equipment.toDto()
        ).run { 
            when {
                isSuccessful -> {
                    body()?.toEntity()?.let {
                        dbDataSource.db.equipmentDao.insert(
                            listOf(it)
                        )    
                    }
                }
                else -> {
                    Log.e(TAG, "updateEquipment: Error: ${equipment.equipmentId}", )
                }
            }
        }
    }

    override suspend fun loadEquipment(studioId: Long, search: String?, type: EquipmentType?): Unit = withContext(Dispatchers.IO) {
        equipmentApi.getAllEquipment(
            studioId = studioId,
            search = if(search?.isEmpty() == true) null else search,
            type = type?.toDb()
        ).run {
            when {
                isSuccessful -> {
                    dbDataSource.db.equipmentDao.syncInsert(
                        body()?.map { it.toEntity() } ?: emptyList()
                    )
                }
                else -> {
                    Log.e(TAG, "loadEquipment: Error: $studioId")
                }
            }
        }
    }

    override suspend fun addToCart(equipmentId: Long): Unit = withContext(Dispatchers.IO) {
        dbDataSource.db.equipmentSelectionDao.insert(EquipmentSelectionEntity(equipmentId = equipmentId))
    }

    override suspend fun removeFromCart(equipmentIds: List<Long>): Unit = withContext(Dispatchers.IO) {
        dbDataSource.db.equipmentSelectionDao.delete(list = equipmentIds)
    }

    override suspend fun getAllSelected(): Flow<List<Long>> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.equipmentSelectionDao.getAll()
            }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun clearSelections() {
        dbDataSource.db.equipmentSelectionDao.clearSelections()
    }
}