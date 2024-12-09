package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class TransportSelectionEntity(
    @PrimaryKey val transportId: Long,
)