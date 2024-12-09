package ua.rikutou.studio.data.datasource.document

import java.util.Date

interface DocumentDataSource {
    suspend fun createDocument(
        dataStart: Date,
        days: Int,
        locations: List<Long>,
        transport: List<Long>,
        equipment: List<Long>,
        studioId: Long,
    )
}