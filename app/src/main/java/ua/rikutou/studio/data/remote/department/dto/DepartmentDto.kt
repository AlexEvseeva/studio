package ua.rikutou.studio.data.remote.department.dto

import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.remote.section.dto.SectionDto

data class DepartmentDto(
    val departmentId: Long,
    val type: String,
    val workHours: String,
    val contactPerson: String,
    val studioId: Long,
    val sections: List<SectionDto>?
)

fun DepartmentDto.toEntity() =
    DepartmentEntity(
        departmentId = departmentId,
        type = type,
        workHours = workHours,
        contactPerson = contactPerson,
        studioId = studioId
    )