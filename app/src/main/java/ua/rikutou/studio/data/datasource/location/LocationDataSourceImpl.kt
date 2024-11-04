package ua.rikutou.studio.data.datasource.location

import android.util.Log
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.local.entity.LocationToGalleryEntity
import ua.rikutou.studio.data.remote.gallery.dto.toEntity
import ua.rikutou.studio.data.remote.location.LocationApi
import ua.rikutou.studio.data.remote.location.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class LocationDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val locationApi: LocationApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : LocationDataSource{
    private val TAG by lazy { LocationDataSourceImpl::class.simpleName }
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getLocationsByStudioId(studioId: Long): Flow<List<Location>> =
        dbDataSource.dbFlow
            .flatMapLatest { db ->
                db.locationDao.getByStudioId(studioId = studioId)
            }.flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }


    override suspend fun loadLocations(studioId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            locationApi.getAllLocations(studioId = studioId).run {
                when {
                    isSuccessful -> {
                        body()?.let { list ->
                            dbDataSource.db.locationDao.insert(
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
                        Log.e(TAG, "loadLocations: error $studioId" )
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
}