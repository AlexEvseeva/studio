package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.rikutou.studio.data.remote.equipment.dto.EquipmentRequest

@Entity
data class EquipmentEntity(
    @PrimaryKey val equipmentId: Long,
    val name: String,
    val type : String,
    val comment: String,
    val rentPrice: Float,
    val studioId: Long,
)

fun EquipmentEntity.toDto() =
    EquipmentRequest(
        equipmentId = if(equipmentId < 0) null else equipmentId,
        name = name,
        type = type,
        comment = comment,
        rentPrice = rentPrice,
        studioId = studioId
    )
