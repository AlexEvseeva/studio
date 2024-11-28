package ua.rikutou.studio.ui.section.details

import ua.rikutou.studio.data.local.dao.SectionDao
import ua.rikutou.studio.data.local.entity.Location
import ua.rikutou.studio.data.local.entity.SectionEntity
import ua.rikutou.studio.navigation.Screen

object SectionDetails {
    sealed interface Event {
        data class OnNavigate(val destination: Screen? = null) : Event
    }

    sealed interface Action {
        data class OnNavigate(val destination: Screen) : Action
        data object OnDelete: Action
    }

    data class State (
        val inProgress: Boolean = false,
        val section: SectionEntity? = null
    )

}