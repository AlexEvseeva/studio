package ua.rikutou.studio.ui.dept.details

import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.navigation.Screen

object DepartmentDetails {
    sealed interface Event {
        data class OnNavigate(val destination: Screen? = null) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen) : Action
        data object OnDelete: Action
    }

    data class State (
        val inProgress: Boolean = false,
        val department: Department? = null
    )
}