package ua.rikutou.studio.data.remote.document

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ua.rikutou.studio.data.remote.document.dto.DocumentDto
import ua.rikutou.studio.data.remote.document.dto.DocumentRequest

interface DocumentApi {
    @POST("document")
    suspend fun createDocument(
        @Body body: DocumentRequest
    ): Response<DocumentDto>
}