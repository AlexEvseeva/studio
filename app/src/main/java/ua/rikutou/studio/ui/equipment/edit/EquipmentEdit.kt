package ua.rikutou.studio.ui.equipment.edit

import ua.rikutou.studio.data.local.entity.EquipmentEntity

object EquipmentEdit {
    sealed interface Event {
        data object OnBack : Event
    }

    sealed interface Action {
        data class OnFieldChanged(
            val name: String? = null,
            val type: String? = null,
            val comment: String? = null,
            val rentPrice: Float? = null,
        ) : Action
        data object OnSave : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val equipment: EquipmentEntity? = null,
    )
}