package ua.rikutou.studio.data.datasource.studio

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.local.entity.StudioEntity

interface StudioDataSource {
    suspend fun getStudioById(studioId: Long): Flow<StudioEntity?>
    suspend fun loadStudioById(studioId: Long)
    suspend fun createUpdateStudio(studio: StudioEntity): Flow<DataSourceResponse<StudioEntity>>
    suspend fun deleteStudio(studioId: Long): Flow<DataSourceResponse<Any>>
}