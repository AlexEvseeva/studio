package ua.rikutou.studio.data.remote.equipment.dto

data class EquipmentRequest(
    val equipmentId: Long? = null,
    val name: String,
    val type : Int,
    val comment: String,
    val rentPrice: Float,
    val studioId: Long,
)
