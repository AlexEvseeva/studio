package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = arrayOf("actorId","phoneId"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = ActorEntity::class,
            parentColumns = arrayOf("actorId"),
            childColumns = arrayOf("actorId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhoneEntity::class,
            parentColumns = arrayOf("phoneId"),
            childColumns = arrayOf("phoneId"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class ActorToPhoneEntity(
    val actorId: Long,
    val phoneId: Long,
)
