package ua.rikutou.studio.ui.document.locations_report

import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.remote.location.dto.LocationsReportRespons
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.document.lease.DocumentCreate
import ua.rikutou.studio.ui.document.lease.DocumentCreate.Action

object LocationsReport {
    sealed interface Event {
        data class OnNavigate(val destination: Screen?) : Event
        data class OnMessage(val message: String) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen?) : Action
        data object OnCreateDocument : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val report: LocationsReportRespons? = null,
    )

}