package ua.rikutou.studio.ui.location.list

import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.navigation.Screen

object LocationList {
    sealed interface Event {
        data class OnNavigate(val destionation: Screen) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destionation: Screen) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val locations: List<Location> = emptyList()
    )
}