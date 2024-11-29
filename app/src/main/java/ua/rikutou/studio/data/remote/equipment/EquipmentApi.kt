package ua.rikutou.studio.data.remote.equipment

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.equipment.dto.EquipmentDto
import ua.rikutou.studio.data.remote.equipment.dto.EquipmentRequest
import ua.rikutou.studio.data.remote.location.dto.LocationRequest
import ua.rikutou.studio.data.remote.location.dto.LocationResponse

interface EquipmentApi {
    @GET("equipment")
    suspend fun getAllEquipment(
        @Query("studioId") studioId: Long,
        @Query("search") search: String? = null,
    ): Response<List<EquipmentDto>>

    @DELETE("equipment/{equipmentId}")
    suspend fun deleteEquipment(
        @Path("equipmentId") equipmentId: Long
    ): Response<Any>

    @POST("equipments")
    suspend fun saveUpdateEquipment(
        @Body body: EquipmentRequest
    ): Response<EquipmentDto>
}