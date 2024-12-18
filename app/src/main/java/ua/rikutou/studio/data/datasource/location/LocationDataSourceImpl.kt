package ua.rikutou.studio.data.datasource.location

import android.util.Log
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.rikutou.studio.data.local.AppDb
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.local.entity.LocationSelectionEntity
import ua.rikutou.studio.data.local.entity.LocationToGalleryEntity
import ua.rikutou.studio.data.local.entity.toDto
import ua.rikutou.studio.data.remote.gallery.dto.toEntity
import ua.rikutou.studio.data.remote.location.LocationApi
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.data.remote.location.dto.LocationsReportRespons
import ua.rikutou.studio.data.remote.location.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class LocationDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val locationApi: LocationApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : LocationDataSource {
    private val TAG by lazy { LocationDataSourceImpl::class.simpleName }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getLocationsByStudioId(studioId: Long): Flow<List<Location>> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.locationDao.getByStudioId(studioId = studioId)
            }.flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getLocationsSelection(): Flow<List<Long>> =
        dbDataSource.dbFlow
            .flatMapLatest<AppDb, List<Long>> { db ->
                db.locationSelectionDao.getSelected()
            }.flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }


    override suspend fun loadLocations(
        studioId: Long,
        search: String,
        type: LocationType?,
        widthFrom: Int?,
        widthTo: Int?,
        lengthFrom: Int?,
        lengthTo: Int?,
        heightFrom: Int?,
        heightTo: Int?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            locationApi.getAllLocations(
                studioId = studioId,
                search = if (search.isEmpty()) null else search,
                type = type?.name,
                widthFrom = widthFrom,
                widthTo = widthTo,
                lengthFrom = lengthFrom,
                lengthTo = lengthTo,
                heightFrom = heightFrom,
                heightTo = heightTo,
            )
                .run {
                    when {
                        isSuccessful -> {
                            body()?.let { list ->
                                dbDataSource.db.locationDao.syncInsert(
                                    list.map {
                                        it.toEntity()
                                    }
                                )
                                dbDataSource.db.galleryDao.insert(
                                    list.flatMap {
                                        it.images
                                    }.toSet().toList().map {
                                        it.toEntity()
                                    }
                                )
                                dbDataSource.db.locationToGalleryDao.insert(
                                    list.flatMap { location ->
                                        location.images.map { image ->
                                            LocationToGalleryEntity(
                                                locationId = location.locationId,
                                                galleryId = image.galleryId
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        else -> {
                            Log.e(TAG, "loadLocations: error $studioId")
                        }
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getLocationById(locationId: Long): Flow<Location> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.locationDao.getByLocationId(locationId = locationId)
            }.flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }

    override suspend fun save(location: LocationEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            locationApi.saveUpdateLocation(
                body = location.toDto()
            ).run {
                when {
                    isSuccessful -> {
                        body()?.let {
                            dbDataSource.db.locationDao.insert(
                                listOf(it.toEntity())
                            )
                        }
                    } else -> {
                        Log.e(TAG, "save: error $location")
                    }
                }
            }
        }
    }

    override suspend fun delete(locationId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            locationApi.deleteLocation(locationId = locationId).run {
                when {
                    code() == 204 -> {  //no content
                        dbDataSource.db.locationDao.deleteById(locationId = locationId)
                    }
                }
            }
        }
    }

    override suspend fun addToCart(locationId: Long): Unit = withContext(Dispatchers.IO) {
        dbDataSource.db.locationSelectionDao.insert(LocationSelectionEntity(locationId = locationId))
    }

    override suspend fun removeFromCart(locationIds: List<Long>): Unit = withContext(Dispatchers.IO) {
        dbDataSource.db.locationSelectionDao.deleteByIds(locationIds = locationIds)
    }

    override suspend fun clearSelection() {
        dbDataSource.db.locationSelectionDao.clearSelections()
    }

    override suspend fun getLocationsReport(studioId: Long): Flow<LocationsReportRespons> = flow {
        locationApi.getLocationReport(studioId = studioId).run {
            when {
                isSuccessful -> {
                    body()?.let {
                        emit(it)
                    }
                }
                else -> {
                    Log.e(TAG, "getLocationsReport: Failed: ${errorBody()?.string()}", )
                }
            }
        }
    }.flowOn(Dispatchers.IO)
    
}