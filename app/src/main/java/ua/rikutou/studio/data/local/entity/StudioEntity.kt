package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.rikutou.studio.data.remote.studio.dto.StudioRequest

@Entity
data class StudioEntity(
    @PrimaryKey val studioId: Long,
    val name: String,
    val address: String,
    val postIndex: String,
    val site: String,
    val youtube: String,
    val facebook: String,
)

fun StudioEntity.toDto() = StudioRequest(
    studioId = studioId,
    name = name,
    address = address,
    postIndex = postIndex,
    site = site,
    youtube = youtube,
    facebook = facebook,
)