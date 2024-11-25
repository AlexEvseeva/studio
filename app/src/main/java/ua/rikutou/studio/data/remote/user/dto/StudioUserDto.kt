package ua.rikutou.studio.data.remote.user.dto

import ua.rikutou.studio.data.local.entity.UserEntity

data class StudioUserDto(
    val userId: Long,
    val userName: String,
    val studioId: Long
)

fun StudioUserDto.toEntity() =
    UserEntity(
        userId = userId,
        name = userName,
        studioId = studioId
    )