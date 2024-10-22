package ua.rikutou.studio.data.remote.studio.dto

import ua.rikutou.studio.data.local.entity.StudioEntity

data class StudioResponse(
    val studioId: Long,
    val name: String,
    val address: String,
    val postIndex: String,
    val site: String,
    val youtube: String,
    val facebook: String,
)

fun StudioResponse.toEntity() =
    StudioEntity(
        studioId = studioId,
        name = name,
        address = address,
        postIndex = postIndex,
        site = site,
        youtube = youtube,
        facebook = facebook,
    )
