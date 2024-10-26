package ua.rikutou.studio.data.remote.location.dto

import ua.rikutou.studio.data.local.entity.LocationEntity

data class LocationResponse(
    val locationId: Long,
    val name: String,
    val address: String,
    val width: Float,
    val length: Float,
    val height: Float,
    val type: String,
    val studioId: Long,
    val rentPrice: Float,
)

fun LocationResponse.toEntity() =
    LocationEntity(
        locationId = locationId,
        name = name,
        address = address,
        width = width,
        length = length,
        height = height,
        type = type,
        studioId = studioId,
        rentPrice = rentPrice,
    )
