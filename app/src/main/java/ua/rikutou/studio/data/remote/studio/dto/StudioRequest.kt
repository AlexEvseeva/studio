package ua.rikutou.studio.data.remote.studio.dto

import ua.rikutou.studio.data.local.entity.StudioEntity

data class StudioRequest(
    val studioId: Long? = null,
    val name: String,
    val address: String,
    val postIndex: String,
    val site: String,
    val youtube: String,
    val facebook: String,
)
fun StudioRequest.toEntity() =
    StudioEntity(
        studioId = studioId ?: -1L,
        name = name,
        address = address,
        postIndex = postIndex,
        site = site,
        youtube = youtube,
        facebook = facebook,
    )
