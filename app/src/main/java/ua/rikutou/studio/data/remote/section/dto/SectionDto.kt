package ua.rikutou.studio.data.remote.section.dto

import ua.rikutou.studio.data.local.entity.SectionEntity

data class SectionDto(
    val sectionId: Long,
    val title: String,
    val address: String,
    val internalPhoneNumber: String,
    val departmentId: Long,
)

fun SectionDto.toEntity() =
    SectionEntity(
        sectionId = sectionId,
        title = title,
        address = address,
        internalPhoneNumber = internalPhoneNumber,
        departmentId = departmentId
    )