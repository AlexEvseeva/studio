package ua.rikutou.studio.data.remote.studio


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.studio.dto.StudioRequest
import ua.rikutou.studio.data.remote.studio.dto.StudioResponse

interface StudioApi {
    @GET("studio")
    suspend fun getStudioById(
        @Query("studioId") studioId: Long
    ): Response<StudioResponse>

    @POST("studio")
    suspend fun createUpdateStudio(
        @Body body: StudioRequest
    ): Response<StudioResponse>
}