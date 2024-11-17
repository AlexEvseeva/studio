package ua.rikutou.studio.data.remote.location.dto

import android.location.LocationRequest
import ua.rikutou.studio.data.local.entity.LocationEntity

data class LocationRequest(
    val locationId: Long? = null,
    val name: String,
    val address: String,
    val width: Float,
    val length: Float,
    val height: Float,
    val type: String,
    val studioId: Long? = null,
    val rentPrice: Float,
)