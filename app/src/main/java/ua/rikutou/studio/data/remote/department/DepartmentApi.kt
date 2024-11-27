package ua.rikutou.studio.data.remote.department

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.department.dto.DepartmentDto

interface DepartmentApi {
    @GET("departments")
    suspend fun getDepartments(
        @Query("studioId") studioId: Long,
        @Query("search") search: String? = null,
    ): Response<List<DepartmentDto>>
}