package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneEntity(
    @PrimaryKey val phoneId: Long,
    val phoneNumber: String,
)
