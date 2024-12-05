package ua.rikutou.studio.ui.document

import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.navigation.Screen

object DocumentCreate {
    sealed class Event {

    }

    sealed interface Action {

    }

    data class State(
        val inProgress: Boolean = false,
        val locations: List<Location> = emptyList(),
        val locationSum: Float = 0F,
    )
}