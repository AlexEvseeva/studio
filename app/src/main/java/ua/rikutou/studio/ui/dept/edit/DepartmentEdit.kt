package ua.rikutou.studio.ui.dept.edit

import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity

object DepartmentEdit {
    sealed interface Event {
        data object OnBack : Event
    }

    sealed interface Action {
        data class OnFieldChanged(
            val type: String? = null,
            val workHours: String? = null,
            val contactPerson: String? = null,
        ) : Action
        data object OnSave : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val department: Department? = null,
    )
}