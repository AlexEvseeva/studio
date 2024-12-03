package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.rikutou.studio.data.remote.transport.TransportType
import ua.rikutou.studio.data.remote.transport.dto.TransportDto
import ua.rikutou.studio.data.remote.transport.dto.TransportRequest
import java.util.Date

@Entity
data class TransportEntity(
    @PrimaryKey val transportId: Long,
    val type: TransportType,
    val mark: String,
    val manufactureDate: Date,
    val seats: Int,
    val departmentId: Long,
    val color: String,
    val technicalState: String,
)

fun TransportEntity.toDto() =
    TransportRequest(
        transportId = transportId,
        type = type.fromTransportType(),
        mark = mark,
        manufactureDate = manufactureDate.time,
        seats = seats,
        departmentId = departmentId,
        color = color,
        technicalState = technicalState
    )