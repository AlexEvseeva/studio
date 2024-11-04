package ua.rikutou.studio.ui.location.details

import androidx.navigation.NavDestination
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.navigation.Screen

object LocationDetails {
    sealed interface Event {
        data class OnNavigate(val destination: Screen) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val location: Location? = null
    )
}