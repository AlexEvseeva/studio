package ua.rikutou.studio.data.datasource.location

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity


interface LocationDataSource {
    suspend fun getLocationsByStudioId(studioId: Long): Flow<List<Location>>
    suspend fun loadLocations(studioId: Long, search: String)
    suspend fun getLocationById(locationId: Long): Flow<Location>
}