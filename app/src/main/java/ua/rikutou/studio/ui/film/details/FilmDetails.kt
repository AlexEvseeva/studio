package ua.rikutou.studio.ui.film.details

import ua.rikutou.studio.data.local.entity.Actor
import ua.rikutou.studio.data.local.entity.Film
import ua.rikutou.studio.navigation.Screen

object FilmDetails {
    sealed interface Event {
        data class OnNavigate(val destination: Screen? = null) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen) : Action
        data object OnDelete: Action
    }

    data class State (
        val inProgress: Boolean = false,
        val film: Film? = null
    )
}