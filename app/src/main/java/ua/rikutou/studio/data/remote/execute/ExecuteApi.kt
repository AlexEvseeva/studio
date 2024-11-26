package ua.rikutou.studio.data.remote.execute

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ua.rikutou.studio.data.remote.execute.dto.ExecuteRequest
import ua.rikutou.studio.data.remote.execute.dto.ExecuteResponse

interface ExecuteApi {
    @POST("execute")
    suspend fun executeQuery(
        @Body body: ExecuteRequest
    ): Response<ExecuteResponse>
}