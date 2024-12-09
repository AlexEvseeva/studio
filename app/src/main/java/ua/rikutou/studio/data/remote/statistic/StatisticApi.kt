package ua.rikutou.studio.data.remote.statistic

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.statistic.dto.Statistic

interface StatisticApi {
    @GET("statistic")
    suspend fun getStatistic(
        @Query("studioId") studioId: Long
    ): Response<Statistic>
}