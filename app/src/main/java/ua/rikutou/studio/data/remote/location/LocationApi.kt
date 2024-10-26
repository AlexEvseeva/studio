package ua.rikutou.studio.data.remote.location

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.location.dto.LocationResponse

interface LocationApi {
    @GET("locations")
    suspend fun getAllLocations(
        @Query("studioId") studioId: Long
    ): Response<List<LocationResponse>>
}