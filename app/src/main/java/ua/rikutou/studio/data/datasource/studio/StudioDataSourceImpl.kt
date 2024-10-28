package ua.rikutou.studio.data.datasource.studio

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
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.data.local.entity.toDto
import ua.rikutou.studio.data.remote.studio.StudioApi
import ua.rikutou.studio.data.remote.studio.dto.StudioRequest
import ua.rikutou.studio.data.remote.studio.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class StudioDataSourceImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val studioApi: StudioApi,
    private val dbDataSource: DbDataSource,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : StudioDataSource {
    private val TAG by lazy { StudioDataSourceImpl::class.simpleName }
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getStudioById(studioId: Long): Flow<StudioEntity?> =
        dbDataSource.dbFlow
            .flatMapLatest { db -> db.studioDao.getById(studioId = studioId)}
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace()}

    override suspend fun loadStudioById(studioId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            studioApi.getStudioById(studioId = studioId).run {
                when {
                    isSuccessful -> {
                        body()?.let {
                            dbDataSource.db.studioDao.insert(it.toEntity())
                        }
                    }
                    else -> {
                        Log.e(TAG, "loadStudioById: error: $studioId")
                    }
                }
            }
        }
    }

    override suspend fun createUpdateStudio(studio: StudioEntity): Flow<DataSourceResponse<StudioEntity>> = flow {
        emit(DataSourceResponse.InProgress)
        studioApi.createUpdateStudio(
            studio.toDto()
        ).run {
            when {
                isSuccessful -> {
                    body()?.let {
                        dbDataSource.db.studioDao.insert(it.toEntity())
                        emit(DataSourceResponse.Success())
                    }
                }
                else -> {
                    Log.e(TAG, "createUpdateStudio: error")
                    emit(DataSourceResponse.Error())
                }
            }
        }
    }
}