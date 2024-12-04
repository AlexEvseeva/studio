package ua.rikutou.studio.data.remote.actor.dto

import ua.rikutou.studio.data.local.entity.ActorEntity
import ua.rikutou.studio.data.remote.actor.phone.dto.PhoneDto
import ua.rikutou.studio.data.remote.film.dto.FilmDto

data class ActorDto(
    val actorId: Long,
    val name: String,
    val nickName: String? = null,
    val role: String? = null,
    val studioId: Long,
    val films: List<FilmDto>? = null,
    val actorFilms: List<ActorToFilmDto>? = null,
    val phones: List<PhoneDto>? = null,
)

fun ActorDto.toEntity() =
    ActorEntity(
        actorId = actorId,
        name = name,
        nickName = nickName,
        role = role,
        studioId = studioId,
    )
