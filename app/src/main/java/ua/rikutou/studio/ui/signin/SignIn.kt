package ua.rikutou.studio.ui.signin

import ua.rikutou.studio.BuildConfig

object SignIn {
    sealed interface Event {
        data class OnMessage(val message: String) : Event
        data class OnNavigate(val route: String): Event
    }

    sealed interface Action {
        data object OnLogin : Action
        data class onNameChanged(val name: String) : Action
        data class onPasswordChanged(val password: String) : Action
        data class OnNavigate(val route: String): Action
    }

    data class State (
        val name: String = if (BuildConfig.DEBUG) "John.Doe@gmail.com" else "",
        val password: String = if (BuildConfig.DEBUG) "Secret1" else "",
        val inProgress: Boolean = false,
    )
}