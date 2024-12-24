package ua.rikutou.studio.ui.transport.edit

import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.local.entity.TransportEntity
import java.util.Date

object TransportEdit {
    sealed interface Event {
        data object OnBack : Event
        data class OmMessage(val message: String): Event
    }

    sealed interface Action {
        data class OnFieldChanged(
            val type: String? = null,
            val mark: String? = null,
            val manufactureDate: Date? = null,
            val seats: Int? = null,
            val color: String? = null,
            val technicalState: String? = null,
            val rentPrice: String? = null,
        ) : Action
        data object OnSave : Action
        data object OnSelectDate : Action
        data class OnDepartmentSelect(val departmentId: Long) : Action
        data object OnDismissDatePicker : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val transport: TransportEntity? = null,
        val departments: List<DepartmentEntity>? = null,
        val isSelectDateDialogActive: Boolean = false
    )
}