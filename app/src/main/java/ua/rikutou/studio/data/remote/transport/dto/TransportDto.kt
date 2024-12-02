package ua.rikutou.studio.data.remote.transport.dto

import ua.rikutou.studio.data.local.entity.TransportEntity
import java.util.Date

data class TransportDto(
    val transportId: Long,
    val type: String,
    val mark: String,
    val manufactureDate: Long,
    val seats: Int,
    val departmentId: Int,
    val color: String,
    val technicalState: String,
)

fun TransportDto.toEntity() =
    TransportEntity(
        transportId = transportId,
        type = type,
        mark = mark,
        manufactureDate = Date(manufactureDate),
        seats = seats,
        departmentId = departmentId,
        color = color,
         technicalState = technicalState
    )