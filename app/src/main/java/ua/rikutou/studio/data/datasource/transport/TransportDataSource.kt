package ua.rikutou.studio.data.datasource.transport

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.TransportEntity

interface TransportDataSource {
    suspend fun getTransport(): Flow<List<TransportEntity>>
    suspend fun getTransportById(transportId: Long): Flow<TransportEntity>
    suspend fun save(transport: TransportEntity)
    suspend fun delete(transportId: Long)
}