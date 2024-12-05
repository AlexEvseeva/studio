package ua.rikutou.studio.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import ua.rikutou.studio.data.remote.department.dto.DepartmentRequest

@Entity
data class DepartmentEntity(
    @PrimaryKey val departmentId: Long,
    val type: String,
    val workHours: String,
    val contactPerson: String,
    val studioId: Long,
)

data class Department(
    @Embedded val entity: DepartmentEntity,
    @Relation(
        entity = SectionEntity::class,
        entityColumn = "departmentId",
        parentColumn = "departmentId"
    ) val sections: List<SectionEntity>,
    @Relation(
        entity = TransportEntity::class,
        entityColumn = "departmentId",
        parentColumn = "departmentId"
    ) val transport: List<TransportEntity>,
    @Relation(
        associateBy = Junction(DepartmentToPhoneEntity::class),
        parentColumn = "departmentId",
        entityColumn = "phoneId"
    ) val phones: List<PhoneEntity>
)

fun DepartmentEntity.toDto() =
    DepartmentRequest(
        departmentId = departmentId,
        type = type,
        workHours = workHours,
        contactPerson = contactPerson,
        studioId = studioId
    )
