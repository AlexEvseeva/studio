package ua.rikutou.studio.data.remote.film.dto

import ua.rikutou.studio.data.local.entity.FilmEntity
import java.util.Date

data class FilmDto(
    val filmId: Long,
    val title: String,
    val genres: List<Int>,
    val director: String,
    val writer: String,
    val date: Long,
    val budget: Float,
)

fun FilmDto.toEntity() =
    FilmEntity(
        filmId = filmId,
        title = title,
        director = director,
        writer = writer,
        date = Date(date),
        budget = budget
    )
