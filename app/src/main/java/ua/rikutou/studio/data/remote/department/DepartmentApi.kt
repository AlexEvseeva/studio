package ua.rikutou.studio.data.remote.department

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.department.dto.DepartmentDto
import ua.rikutou.studio.data.remote.department.dto.DepartmentRequest

interface DepartmentApi {
    @GET("departments")
    suspend fun getDepartments(
        @Query("studioId") studioId: Long,
        @Query("search") search: String? = null,
    ): Response<List<DepartmentDto>>

    @DELETE("departments/{departmentId}")
    suspend fun deleteDepartment(
        @Path("departmentId") departmentId: Long
    ): Response<Any>

    @POST("departments")
    suspend fun saveUpdateDepartment(
        @Body body: DepartmentRequest
    ): Response<DepartmentDto>
}