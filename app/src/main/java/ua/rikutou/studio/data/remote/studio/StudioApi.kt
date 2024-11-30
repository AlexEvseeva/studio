package ua.rikutou.studio.data.remote.studio


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @DELETE("studio/{studioId}")
    suspend fun deleteStudio(
        @Path("studioId") studioId: Long
    ): Response<Any>
}