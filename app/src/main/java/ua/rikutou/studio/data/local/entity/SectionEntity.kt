package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SectionEntity(
    @PrimaryKey val sectionId: Long,
    val title: String,
    val address: String,
    val internalPhoneNumber: String,
    val departmentId: Long,
)
