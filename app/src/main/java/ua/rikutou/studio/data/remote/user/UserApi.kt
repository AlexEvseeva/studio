package ua.rikutou.studio.data.remote.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.user.dto.StudioUserDto

interface UserApi {
    @GET("users")
    suspend fun getStudioUsers(
        @Query("studioId") studioId: Long,
    ): Response<List<StudioUserDto>>

    @DELETE("users/{userId}")
    suspend fun deleteUserById(
        @Path("userId") userId: Long
    ): Response<Any>

    @POST("users")
    suspend fun updateUserStudio(
        @Body body: StudioUserDto
    ): Response<StudioUserDto>
}