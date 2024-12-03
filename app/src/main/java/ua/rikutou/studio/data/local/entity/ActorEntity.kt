package ua.rikutou.studio.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
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
)

fun ActorEntity.toDto() =
    ActorRequest(
        actorId = actorId,
        name = name,
        nickName = nickName,
        role = role,
        studioId = studioId
    )
