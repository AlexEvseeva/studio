package ua.rikutou.studio.ui.equipment.details

import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.navigation.Screen

object EquipmentDetails {
    sealed interface Event {
        data class OnNavigate(val destination: Screen? = null) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen) : Action
        data object OnDelete: Action
    }

    data class State (
        val inProgress: Boolean = false,
        val equipment: EquipmentEntity? = null
    )
}