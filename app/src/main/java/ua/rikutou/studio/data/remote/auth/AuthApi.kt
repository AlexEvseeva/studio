package ua.rikutou.studio.data.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ua.rikutou.studio.data.remote.auth.dto.AuthRequest

interface AuthApi {
    @POST("signup")
    suspend fun signUp(
        @Body body: AuthRequest
    ): Response<Any>
}