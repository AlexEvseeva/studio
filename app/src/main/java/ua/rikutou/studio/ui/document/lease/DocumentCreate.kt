package ua.rikutou.studio.ui.document.lease

import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.navigation.Screen
import java.util.Date

object DocumentCreate {
    sealed interface Event {
        data class OnNavigate(val destination: Screen?) : Event
        data class OnMessage(val message: String) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen?) : Action
        data class OnRemoveLocationFromCart(val locationId: Long) : Action
        data class OnRemoveTransportFromCart(val transportId: Long) : Action
        data class OnRemoveEquipmentFromCart(val equipmentId: Long) : Action
        data class OnSelectFromDate(val time: Long) : Action
        data class OnSelectToDays(val days: Int) : Action
        data object OnCreateDocument : Action
        data class OnMessage(val message: String) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val studio: StudioEntity? = null,
        val locations: List<Location> = emptyList(),
        val locationSum: Float = 0F,
        val transport: List<TransportEntity> = emptyList(),
        val transportSum: Float = 0F,
        val equipment: List<EquipmentEntity> = emptyList(),
        val equipmentSum: Float = 0F,
        val fromDate: Date? = null,
        val toDays: Int = 1,
    )
}