package ua.rikutou.studio.data.datasource.location

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.data.remote.location.dto.LocationsReportRespons


interface LocationDataSource {
    suspend fun getLocationsByStudioId(studioId: Long): Flow<List<Location>>
    suspend fun getLocationsSelection(): Flow<List<Long>>
    suspend fun loadLocations(
        studioId: Long,
        search: String,
        type: LocationType? = null,
        widthFrom: Int? = null,
        widthTo: Int? = null,
        lengthFrom: Int? = null,
        lengthTo: Int? = null,
        heightFrom: Int? = null,
        heightTo: Int? = null
    )
    suspend fun getLocationById(locationId: Long): Flow<Location>
    suspend fun save(location: LocationEntity)
    suspend fun delete(locationId: Long)
    suspend fun addToCart(locationId: Long)
    suspend fun removeFromCart(locationIds: List<Long>)
    suspend fun clearSelection()

    suspend fun getLocationsReport(studioId: Long): Flow<LocationsReportRespons>
}