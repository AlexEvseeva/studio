package ua.rikutou.studio.data.remote.actor.dto

import ua.rikutou.studio.data.local.entity.Actor
import ua.rikutou.studio.data.local.entity.ActorEntity

data class ActorDto(
    val actorId: Long,
    val name: String,
    val nickName: String? = null,
    val role: String? = null,
    val studioId: Long,
)

fun ActorDto.toEntity() =
    ActorEntity(
        actorId = actorId,
        name = name,
        nickName = nickName,
        role = role,
        studioId = studioId
    )
