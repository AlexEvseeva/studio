package ua.rikutou.studio.data.remote.section

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import ua.rikutou.studio.data.remote.section.dto.SectionDto
import ua.rikutou.studio.data.remote.section.dto.SectionRequest

interface SectionApi {
    @POST("sections")
    suspend fun insertUpdateSection(
        @Body body: SectionRequest
    ): Response<SectionDto>

    @DELETE("sections/{sectionId}")
    suspend fun deleteSectionById(
        @Path("sectionId") sectionId: Long
    ): Response<Nothing>

}