package ua.rikutou.studio.data.datasource.execute

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.remote.execute.dto.ExecuteResponse

interface ExecuteDataSource {
    suspend fun executeQuery(query: String): Flow<DataSourceResponse<ExecuteResponse>>
}