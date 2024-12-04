package ua.rikutou.studio.data.remote.actor.phone.dto

import ua.rikutou.studio.data.local.entity.ActorToPhoneEntity
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

fun PhoneDto.toRefEntity(actorId: Long) =
    ActorToPhoneEntity(
        actorId = actorId,
        phoneId = phoneId
    )
