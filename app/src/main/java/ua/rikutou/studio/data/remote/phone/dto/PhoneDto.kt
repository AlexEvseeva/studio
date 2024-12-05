package ua.rikutou.studio.data.remote.phone.dto

import ua.rikutou.studio.data.local.entity.ActorToPhoneEntity
import ua.rikutou.studio.data.local.entity.DepartmentToPhoneEntity
import ua.rikutou.studio.data.local.entity.PhoneEntity

data class PhoneDto(
    val phoneId: Long,
    val phoneNumber: String,
)

fun PhoneDto.toEntity() =
    PhoneEntity(
        phoneId = phoneId,
        phoneNumber = phoneNumber
    )

fun PhoneDto.toActorRefEntity(actorId: Long) =
    ActorToPhoneEntity(
        actorId = actorId,
        phoneId = phoneId
    )

fun PhoneDto.toDepartmentRefEntity(departmentId: Long) =
    DepartmentToPhoneEntity(
        departmentId = departmentId,
        phoneId = phoneId
    )


