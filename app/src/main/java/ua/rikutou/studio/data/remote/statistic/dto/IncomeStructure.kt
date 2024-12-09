package ua.rikutou.studio.data.remote.statistic.dto

data class IncomeStructure(
    val byLocation: Float? = null,
    val byTransport: Float? = null,
    val byEquipment: Float? = null,
    val total: Float? = null,
)
