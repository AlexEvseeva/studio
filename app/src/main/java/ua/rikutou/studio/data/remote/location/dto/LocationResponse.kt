package ua.rikutou.studio.data.remote.location.dto

import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.remote.gallery.dto.GalleryResponse
import ua.rikutou.studio.data.remote.location.LocationType

data class LocationResponse(
    val locationId: Long,
    val name: String,
    val address: String,
    val width: Float,
    val length: Float,
    val height: Float,
    val type: LocationType,
    val studioId: Long,
    val rentPrice: Float,
    val images: List<GalleryResponse>
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