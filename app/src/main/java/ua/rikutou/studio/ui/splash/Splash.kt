package ua.rikutou.studio.ui.splash

import ua.rikutou.studio.navigation.Screen


object Splash {
    sealed interface Event {
        data class OnNavigate(val destination: Screen): Event
    }

    data class State(
        val inProgress: Boolean = false,
    )
}