package ua.rikutou.studio.ui.location.edit

import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.navigation.Screen

object LocationEdit {
    sealed interface Event {
        data object OnBack : Event
    }

    sealed interface Action {
        data class OnFieldChanged(
            val name: String? = null,
            val address: String? = null,
            val width: String? = null,
            val length: String? = null,
            val height: String? = null,
            val rentPrice: String? = null,
        ) : Action
        data object OnSave : Action
        data class OnTypeSelected(val option: String) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val location: LocationEntity? = null,
    )
}