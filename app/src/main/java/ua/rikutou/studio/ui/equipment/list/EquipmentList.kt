package ua.rikutou.studio.ui.equipment.list

import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.navigation.Screen

object EquipmentList {
    sealed interface Event {
        data class OnNavigate(val destionation: Screen) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destionation: Screen) : Action
        data class OnSearchChanged(val search: String) : Action
        data object OnSearch : Action
        data object OnCancel : Action
        data class OnAddToCart(val equipmentId: Long) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val equipment: List<EquipmentHolder> = emptyList(),
        val isSearchActive: Boolean = false,
        val isSearchEnabled: Boolean = true,
    )
}