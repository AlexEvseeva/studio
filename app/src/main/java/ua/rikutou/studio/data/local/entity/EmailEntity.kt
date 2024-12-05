package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmailEntity(
    @PrimaryKey val emailId: Long,
    val email: String
)
