package ua.rikutou.studio.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity
data class FilmEntity(
    @PrimaryKey val filmId: Long,
    val title: String,
    val director: String,
    val writer: String,
    val date: Date,
    val budget: Float,
)

data class Film(
    @Embedded val entity: FilmEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "genre",
        associateBy = Junction(FilmToGenreEntity::class)
    ) val genres: List<GenreEntity>
)
