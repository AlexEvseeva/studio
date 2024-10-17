package ua.rikutou.studio.data.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ua.rikutou.studio.data.remote.auth.dto.AuthRequest
import ua.rikutou.studio.data.remote.auth.dto.SignInResponse

interface AuthApi {
    @POST("signup")
    suspend fun signUp(
        @Body body: AuthRequest
    ): Response<Any>

    @POST("signin")
    suspend fun signIn(
        @Body body: AuthRequest
    ): Response<SignInResponse>

    @GET("me")
    suspend fun getMe(): Response<SignInResponse>
}