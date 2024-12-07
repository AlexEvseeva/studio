package ua.rikutou.studio.ui.transport.list

import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.data.remote.transport.TransportType
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.location.list.Dimensions
import java.util.Date

object TransportList {
    sealed interface Event {
        data class OnNavigate(val destionation: Screen) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destionation: Screen) : Action
        data class OnSearchChanged(val search: String) : Action
        data object OnSearch : Action
        data object OnCancel : Action
        data object OnClearFilters : Action
        data class OnTypeSelect(val type: TransportType?) : Action
        data class OnFieldSelect(
            val dateFrom: Date? = null,
            val dateTo: Date? = null,
            val seatsFrom: Int? = null,
            val seatsTo: Int? = null
        ) : Action
        data class OnAddToCart(val transportId: Long) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val transport: List<TransportHolder> = emptyList(),
        val isSearchActive: Boolean = false,
        val isSearchEnabled: Boolean = true,
        val typeFilter: TransportType? = null,
        val selectedTransport: List<Long> = emptyList()
    )
}