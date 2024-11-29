package ua.rikutou.studio.data.remote.equipment.dto

import ua.rikutou.studio.data.local.entity.EquipmentEntity

data class EquipmentDto(
    val equipmentId: Long,
    val name: String,
    val type : String,
    val comment: String,
    val rentPrice: Float,
    val studioId: Long,
)

fun EquipmentDto.toEntity() =
    EquipmentEntity(
        equipmentId = equipmentId,
        name = name,
        type = type,
        comment = comment,
        rentPrice = rentPrice,
        studioId = studioId
    )