package ua.rikutou.studio.data.remote.department.dto

data class DepartmentRequest(
    val departmentId: Long?,
    val type: String,
    val workHours: String,
    val contactPerson: String,
    val studioId: Long,
)
