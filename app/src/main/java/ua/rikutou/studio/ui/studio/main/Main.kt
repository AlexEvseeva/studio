package ua.rikutou.studio.ui.studio.main

import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.data.remote.statistic.dto.Statistic
import ua.rikutou.studio.navigation.Screen

object Main {
    sealed interface Event {
        data class OnNavigate(val destination: Screen) : Event
        data class OnMessage(val message: String) : Event
    }

    sealed interface Action {
        data object OnEdit : Action
        data class OnNavigate(val destination: Screen) : Action
    }

    data class State (
        val inProgress: Boolean = false,
        val studio: StudioEntity? = null,
        val statistic: Statistic? = null,
    )

}