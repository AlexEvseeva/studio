package ua.rikutou.studio.data.remote.actor.dto

import ua.rikutou.studio.data.local.entity.ActorToFilmEntity

data class ActorToFilmDto(
    val actorId: Long,
    val filmId: Long,
    val role: String,
)

fun ActorToFilmDto.toEntity() =
    ActorToFilmEntity(
        actorId = actorId,
        filmId = filmId,
        role = role
    )