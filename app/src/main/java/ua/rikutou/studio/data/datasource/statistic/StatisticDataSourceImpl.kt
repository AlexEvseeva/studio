package ua.rikutou.studio.data.datasource.statistic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.remote.parseError
import ua.rikutou.studio.data.remote.parseErrorMessage
import ua.rikutou.studio.data.remote.statistic.StatisticApi
import ua.rikutou.studio.data.remote.statistic.dto.Statistic

class StatisticDataSourceImpl(
    private val statisticApi: StatisticApi
) : StatisticDataSource {

    override suspend fun getStatistic(studioId: Long): Flow<DataSourceResponse<Statistic>> = flow {
        emit(DataSourceResponse.InProgress)
        statisticApi.getStatistic(studioId = studioId).run {
            when {
                isSuccessful -> {
                    body()?.let {
                        emit(DataSourceResponse.Success(payload = it))
                    }
                }
                else -> {
                    emit(
                        DataSourceResponse.Error<String>(message = parseErrorMessage(errorBody = errorBody()))
                    )
                }
            }
        }
    }
}