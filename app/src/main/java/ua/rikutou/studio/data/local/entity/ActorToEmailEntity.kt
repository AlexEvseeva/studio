package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["actorId","emailId"],
    foreignKeys = [
        ForeignKey(
            entity = ActorEntity::class,
            parentColumns = ["actorId"],
            childColumns = ["actorId"]
        ),
        ForeignKey(
            entity = EmailEntity::class,
            parentColumns = ["emailId"],
            childColumns = ["emailId"]
        )
    ]
)
data class ActorToEmailEntity(
    val actorId: Long,
    val emailId: Long,
)
