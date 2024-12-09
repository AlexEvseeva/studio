package ua.rikutou.studio.data.remote.document.dto

data class DocumentRequest(
    val dateStart: Long,
    val days: Int,
    val locations: List<Long>,
    val transport: List<Long>,
    val equipment: List<Long>,
)