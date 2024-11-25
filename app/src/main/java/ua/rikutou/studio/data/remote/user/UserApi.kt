package ua.rikutou.studio.data.remote.user

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.user.dto.StudioUserDto

interface UserApi {
    @GET("users")
    suspend fun getStudioUsers(
        @Query("studioId") studioId: Long,
    ): Response<List<StudioUserDto>>
}