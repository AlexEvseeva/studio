package ua.rikutou.studio.data.remote.transport

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.transport.dto.TransportDto
import ua.rikutou.studio.data.remote.transport.dto.TransportRequest
import java.util.Date

interface TransportApi {
    @GET("transport")
    suspend fun getTransport(
        @Query("studioId") studioId: Long,
        @Query("search") search: String?,
        @Query("type") type: Int?,
        @Query("manufactureDateFrom") manufactureDateFrom: Long?,
        @Query("manufactureDateTo") manufactureDateTo: Long?,
        @Query("seatsFrom") seatsFrom: Int?,
        @Query("seatsTo") seatsTo: Int?
    ): Response<List<TransportDto>>

    @DELETE("transport/{transportId}")
    suspend fun deleteTransport(
        @Path("transportId") transportId: Long
    ): Response<Any>

    @POST("transport")
    suspend fun saveUpdateTransport(
        @Body body: TransportRequest
    ): Response<TransportDto>
}