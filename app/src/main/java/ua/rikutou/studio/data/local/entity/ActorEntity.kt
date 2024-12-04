package ua.rikutou.studio.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import ua.rikutou.studio.data.remote.actor.dto.ActorRequest

@Entity
data class ActorEntity(
    @PrimaryKey val actorId: Long,
    val name: String,
    val nickName: String? = null,
    val role: String? = null,
    val studioId: Long,
)

data class Actor(
    @Embedded val entity: ActorEntity,
    @Relation(
        parentColumn = "actorId",
        entityColumn = "filmId",
        associateBy = Junction(ActorToFilmEntity::class)
    ) val films: List<FilmEntity>,
    @Relation(
        parentColumn = "actorId",
        entityColumn = "phoneId",
        associateBy = Junction(ActorToPhoneEntity::class)
    ) val phones: List<PhoneEntity>
)

fun ActorEntity.toDto() =
    ActorRequest(
        actorId = actorId,
        name = name,
        nickName = nickName,
        role = role,
        studioId = studioId
    )
