package ua.rikutou.studio.data.remote.department.dto

import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.remote.phone.dto.PhoneDto
import ua.rikutou.studio.data.remote.section.dto.SectionDto
import ua.rikutou.studio.data.remote.transport.dto.TransportDto

data class DepartmentDto(
    val departmentId: Long,
    val type: String,
    val workHours: String,
    val contactPerson: String,
    val studioId: Long,
    val sections: List<SectionDto>?,
    val transport: List<TransportDto>?,
    val phones: List<PhoneDto>?,
)

fun DepartmentDto.toEntity() =
    DepartmentEntity(
        departmentId = departmentId,
        type = type,
        workHours = workHours,
        contactPerson = contactPerson,
        studioId = studioId
    )