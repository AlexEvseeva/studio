package ua.rikutou.studio.data.remote.email.dto

import ua.rikutou.studio.data.local.entity.ActorToEmailEntity
import ua.rikutou.studio.data.local.entity.DepartmentToEmailEntity
import ua.rikutou.studio.data.local.entity.EmailEntity

data class EmailDto(
    val emailId: Long,
    val email: String
)

fun EmailDto.toEntity() =
    EmailEntity(
        emailId = emailId,
        email = email
    )

fun EmailDto.toActorRerEntity(actorId: Long) =
    ActorToEmailEntity(
        actorId = actorId,
        emailId = emailId
    )

fun EmailDto.toDepartmentRefEntity(departmentId: Long) =
    DepartmentToEmailEntity(
        departmentId = departmentId,
        emailId = emailId
    )
