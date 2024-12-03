package ua.rikutou.studio.data.remote.actor

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ua.rikutou.studio.data.remote.actor.dto.ActorDto
import ua.rikutou.studio.data.remote.actor.dto.ActorRequest

interface ActorApi {
    @GET("actor")
    suspend fun getActors(
        @Query("studioId") studioId: Long,
        @Query("search") search: String? = null
    ): Response<List<ActorDto>>

    @POST("actor")
    suspend fun createUpdate(
        @Body body: ActorRequest
    ): Response<ActorDto>

    @DELETE("actor/{actorId}")
    suspend fun delete(
        @Path("actorId") actorId: Long
    ): Response<Any>
}