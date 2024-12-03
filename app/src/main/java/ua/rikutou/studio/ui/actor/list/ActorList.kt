package ua.rikutou.studio.ui.actor.list

import ua.rikutou.studio.data.local.entity.Actor
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.location.list.Dimensions

object ActorList {
    sealed interface Event {
        data class OnNavigate(val destionation: Screen) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destionation: Screen) : Action
        data class OnSearchChanged(val search: String) : Action
        data object OnSearch : Action
        data object OnCancel : Action
        data object OnClearFilters : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val actors: List<Actor> = emptyList(),
        val isSearchActive: Boolean = false,
        val isSearchEnabled: Boolean = true,
    )
}