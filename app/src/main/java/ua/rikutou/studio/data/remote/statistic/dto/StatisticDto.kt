package ua.rikutou.studio.data.remote.statistic.dto

import ua.rikutou.studio.data.remote.actor.dto.ActorDto
import ua.rikutou.studio.data.remote.equipment.dto.EquipmentDto
import ua.rikutou.studio.data.remote.location.dto.LocationResponse
import ua.rikutou.studio.data.remote.transport.dto.TransportDto

data class Statistic(
    val location: MinAvgMax? = null,
    val transport: MinAvgMax? = null,
    val equipment: MinAvgMax? = null,
    val incomeStructure: IncomeStructure? = null,
    val documentsTotal: Int? = null,
    val mostPopularLocations: List<LocationResponse>? = null,
    val mostPopularTransport: List<TransportDto>? = null,
    val mostPopularEquipment: List<EquipmentDto>? = null,
    val mostPopularActor: ActorDto? = null,
)
