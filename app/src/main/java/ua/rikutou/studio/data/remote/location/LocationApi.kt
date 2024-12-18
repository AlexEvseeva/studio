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
import ua.rikutou.studio.data.remote.location.dto.LocationsReportRespons

interface LocationApi {
    @GET("locations")
    suspend fun getAllLocations(
        @Query("studioId") studioId: Long,
        @Query("search") search: String? = null,
        @Query("type") type: String? = null,
        @Query("widthFrom") widthFrom: Int? = null,
        @Query("widthTo") widthTo: Int? = null,
        @Query("lengthFrom") lengthFrom: Int? = null,
        @Query("lengthTo") lengthTo: Int? = null,
        @Query("heightFrom") heightFrom: Int? = null,
        @Query("heightTo") heightTo: Int? = null
    ): Response<List<LocationResponse>>

    @DELETE("locations/{locationId}")
    suspend fun deleteLocation(
        @Path("locationId") locationId: Long
    ): Response<Any>

    @POST("locations")
    suspend fun saveUpdateLocation(
        @Body body: LocationRequest
    ): Response<LocationResponse>

    @GET("reportLocation")
    suspend fun getLocationReport(
        @Query("studioId") studioId: Long
    ): Response<LocationsReportRespons>
}