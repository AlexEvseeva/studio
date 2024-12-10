package ua.rikutou.studio.data.remote.transport.dto

data class TransportRequest(
    val transportId: Long? = null,
    val type: Int,
    val mark: String,
    val manufactureDate: Long,
    val seats: Int,
    val departmentId: Long,
    val color: String,
    val technicalState: String,
    val rentPrice: Float
)
