package ua.rikutou.studio.ui.section.edit

import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.local.entity.SectionEntity

object SectionEdit {
    sealed interface Event {
        data object OnBack : Event
    }

    sealed interface Action {
        data class OnFieldChanged(
            val title: String? = null,
            val address: String? = null,
            val internalPhoneNumber: String? = null,
        ) : Action
        data object OnSave : Action
        data class OnDepartmentSelect(val departmentId: Long) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val section: SectionEntity? = null,
        val departments: List<DepartmentEntity>? = null
    )
}