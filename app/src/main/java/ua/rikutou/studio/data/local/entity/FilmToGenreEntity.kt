package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import ua.rikutou.studio.data.remote.film.Genre

@Entity(primaryKeys = ["filmId","genre"])
data class FilmToGenreEntity(
    val filmId: Long,
    val genre: Genre
)
