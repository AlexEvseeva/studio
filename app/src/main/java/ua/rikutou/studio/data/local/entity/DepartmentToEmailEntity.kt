package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["departmentId","emailId"],
    foreignKeys = [
        ForeignKey(
            entity = DepartmentEntity::class,
            parentColumns = ["departmentId"],
            childColumns = ["departmentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EmailEntity::class,
            parentColumns = ["emailId"],
            childColumns = ["emailId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DepartmentToEmailEntity(
    val departmentId: Long,
    val emailId: Long,
)
