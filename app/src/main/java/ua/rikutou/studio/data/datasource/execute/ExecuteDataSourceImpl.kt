package ua.rikutou.studio.data.datasource.execute

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.remote.execute.ExecuteApi
import ua.rikutou.studio.data.remote.execute.dto.ExecuteRequest
import ua.rikutou.studio.data.remote.execute.dto.ExecuteResponse

class ExecuteDataSourceImpl(
    private val executeApi: ExecuteApi
) : ExecuteDataSource{
    override suspend fun executeQuery(query: String): Flow<DataSourceResponse<ExecuteResponse>> = flow {
        emit(DataSourceResponse.InProgress)
        executeApi.executeQuery(
            body = ExecuteRequest(
                query = query
            )
        ).run {
            emit(
                when {
                    isSuccessful -> {
                        body()?.let {
                            DataSourceResponse.Success(payload = it)
                        }
                    }
                    else -> {
                        DataSourceResponse.Error<Any>(message = "Execution error")
                    }
                } ?: DataSourceResponse.Error<Any>()
            )
        }
    }
}