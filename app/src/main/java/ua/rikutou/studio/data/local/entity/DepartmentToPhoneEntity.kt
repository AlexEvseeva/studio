package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["departmentId", "phoneId"],
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = arrayOf("departmentId"),
            childColumns = arrayOf("departmentId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhoneEntity::class,
            parentColumns = arrayOf("phoneId"),
            childColumns = arrayOf("phoneId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DepartmentToPhoneEntity(
    val departmentId: Long,
    val phoneId: Long,
)
