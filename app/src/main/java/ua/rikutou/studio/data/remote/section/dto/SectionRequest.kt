package ua.rikutou.studio.data.remote.section.dto

data class SectionRequest(
    val sectionId: Long?,
    val title: String,
    val address: String,
    val internalPhoneNumber: String,
    val departmentId: Long,
)
