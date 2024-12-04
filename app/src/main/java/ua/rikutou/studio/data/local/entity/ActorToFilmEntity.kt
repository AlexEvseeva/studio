package ua.rikutou.studio.data.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["actorId","filmId"])
data class ActorToFilmEntity(
    val actorId: Long,
    val filmId: Long,
    val role: String
)
