package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.rikutou.studio.data.remote.film.Genre

@Entity
data class GenreEntity(
    @PrimaryKey val genre: Genre
)
