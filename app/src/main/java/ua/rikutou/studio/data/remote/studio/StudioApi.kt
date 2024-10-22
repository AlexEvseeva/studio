package ua.rikutou.studio.data.remote.studio


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.studio.dto.StudioResponse

interface StudioApi {
    @GET("studio")
    suspend fun getStudioById(
        @Query("studioId") studioId: Long
    ): Response<StudioResponse>
}