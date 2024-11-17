package ua.rikutou.studio.data.remote.location

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.location.dto.LocationRequest
import ua.rikutou.studio.data.remote.location.dto.LocationResponse

interface LocationApi {
    @GET("locations")
    suspend fun getAllLocations(
        @Query("studioId") studioId: Long,
        @Query("search") search: String? = null,
    ): Response<List<LocationResponse>>

    @DELETE("locations/{locationId}")
    suspend fun deleteLocation(
        @Path("locationId") locationId: Long
    ): Response<Any>

    @POST("locations")
    suspend fun saveUpdateLocation(
        @Body body: LocationRequest
    ): Response<LocationResponse>
}