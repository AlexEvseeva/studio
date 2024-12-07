package ua.rikutou.studio.data.datasource.transport

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.data.remote.transport.TransportType
import java.util.Date

interface TransportDataSource {
    suspend fun getTransport(): Flow<List<TransportEntity>>
    suspend fun getTransportById(transportId: Long): Flow<TransportEntity>
    suspend fun save(transport: TransportEntity)
    suspend fun delete(transportId: Long)
    suspend fun loadTransport(
        studioId: Long,
        search: String? = null,
        type: TransportType? = null,
        manufactureDateFrom: Date? = null,
        manufactureDateTo: Date? = null,
        seatsFrom: Int? = null,
        seatsTo: Int? = null,
    )
    suspend fun getSelections(): Flow<List<Long>>
    suspend fun addToCart(transportId: Long)
    suspend fun removeFromCart(transportIds: List<Long>)
}