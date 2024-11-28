package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.rikutou.studio.data.remote.section.dto.SectionRequest

@Entity
data class SectionEntity(
    @PrimaryKey val sectionId: Long,
    val title: String,
    val address: String,
    val internalPhoneNumber: String,
    val departmentId: Long,
)

fun SectionEntity.toDto() =
    SectionRequest(
        sectionId = if(sectionId < 0) null else sectionId,
        title = title,
        address = address,
        internalPhoneNumber = internalPhoneNumber,
        departmentId = departmentId
    )
