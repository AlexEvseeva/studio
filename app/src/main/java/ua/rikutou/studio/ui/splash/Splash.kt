package ua.rikutou.studio.ui.splash



object Splash {
    sealed interface Event {
        data class OnNavigate(val route: String): Event
    }

    data class State(
        val inProgress: Boolean = false,
    )
}