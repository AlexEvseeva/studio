package ua.rikutou.studio.ui.location.list

import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.navigation.Screen

object LocationList {
    sealed interface Event {
        data class OnNavigate(val destionation: Screen) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destionation: Screen) : Action
        data class OnSearchChanged(val search: String) : Action
        data object OnSearch : Action
        data object OnCancel : Action
        data class OnTypeSelect(val type: LocationType?) : Action
        data object OnClearFilters : Action
        data class OnDimansionsChange(val dimensions: Dimensions) : Action
        data class OnAddToCart(val locationId: Long) : Action
        data class OnOrderChange(val order: LocationOrder) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val locationsHolder: List<LocationHolder> = emptyList(),
        val isSearchActive: Boolean = false,
        val isSearchEnabled: Boolean = true,
        val typeFilter: LocationType? = null,
    )
}