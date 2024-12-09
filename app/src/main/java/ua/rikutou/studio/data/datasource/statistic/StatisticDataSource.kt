package ua.rikutou.studio.data.datasource.statistic

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.remote.statistic.dto.Statistic

interface StatisticDataSource {
    suspend fun getStatistic(studioId: Long): Flow<DataSourceResponse<Statistic>>
}