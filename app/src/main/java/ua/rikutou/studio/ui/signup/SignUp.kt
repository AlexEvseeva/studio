package ua.rikutou.studio.ui.signup

import ua.rikutou.studio.BuildConfig
import ua.rikutou.studio.navigation.Screen

object SignUp {
    sealed interface Event {
        data class OnMessage(val message: String) : Event
        data class OnNavigate(val destination: Screen): Event
    }

    sealed interface Action {
        data object OnRegister : Action
        data class onNameChanged(val name: String) : Action
        data class onPasswordChanged(val password: String) : Action
        data class OnNavigate(val destination: Screen): Action
    }

    data class State(
        val name: String = if (BuildConfig.DEBUG) "John.Doe@gmail.com" else "",
        val password: String = if (BuildConfig.DEBUG) "Secret1" else "",
        val inProgress: Boolean = false,
    )
}