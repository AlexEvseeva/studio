package ua.rikutou.studio.ui.actor.detail

import ua.rikutou.studio.data.local.entity.Actor
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.navigation.Screen

object ActorDetails {
    sealed interface Event {
        data class OnNavigate(val destination: Screen? = null) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen) : Action
        data object OnDelete: Action
    }

    data class State (
        val inProgress: Boolean = false,
        val actor: Actor? = null
    )
}