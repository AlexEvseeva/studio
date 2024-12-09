package ua.rikutou.studio.data.datasource.document

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.rikutou.studio.data.remote.document.DocumentApi
import ua.rikutou.studio.data.remote.document.dto.DocumentRequest
import java.util.Date

class DocumentDataSourceImpl(
    private val documentApi: DocumentApi
) : DocumentDataSource {
    private val TAG by lazy { DocumentDataSourceImpl::class.simpleName }

    override suspend fun createDocument(
        dataStart: Date,
        days: Int,
        locations: List<Long>,
        transport: List<Long>,
        equipment: List<Long>
    ): Unit = withContext(Dispatchers.IO) {
        documentApi.createDocument(
            body = DocumentRequest(
                dateStart = dataStart.time,
                days = days,
                locations = locations,
                transport = transport,
                equipment = equipment
            )
        ).run {
            when {
                isSuccessful -> {}
                else -> {
                    Log.e(TAG, "createDocument: ${errorBody()?.string()}")
                }
            }
        }
    }
}