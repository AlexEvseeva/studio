@file:OptIn(ExperimentalCoroutinesApi::class)

package ua.rikutou.studio.data.datasource.transport

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
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.data.local.entity.toDto
import ua.rikutou.studio.data.remote.transport.TransportApi
import ua.rikutou.studio.data.remote.transport.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class TransportDataSourceImpl constructor(
    private val transportApi: TransportApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : TransportDataSource{
    private val TAG by lazy { TransportDataSourceImpl::class.simpleName }

    override suspend fun getTransport(): Flow<List<TransportEntity>> =
        dbDataSource.dbFlow.flatMapLatest { db ->
            db.transportDao.getAll()
        }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun getTransportById(transportId: Long): Flow<TransportEntity> =
        dbDataSource.dbFlow.flatMapLatest { db ->
            db.transportDao.getById(transportId = transportId)
        }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun save(transport: TransportEntity): Unit = withContext(Dispatchers.IO) {
        transportApi.saveUpdateTransport(body = transport.toDto()).run {
            when {
                isSuccessful -> {
                    dbDataSource.db.transportDao.insert(
                        body()?.toEntity()?.let {
                            listOf(it)
                        } ?: emptyList()
                    )
                }
                else -> {
                    Log.e(TAG, "save: Error: $transport")
                }
            }
        }
    }

    override suspend fun delete(transportId: Long): Unit = withContext(Dispatchers.IO) {
        transportApi.deleteTransport(transportId = transportId).run {
            dbDataSource.db.transportDao.deleteById(transportId = transportId)
            when {
                isSuccessful -> {}
                else -> {
                    Log.e(TAG, "delete: Error: $transportId", )
                }
            }
        }
    }
}