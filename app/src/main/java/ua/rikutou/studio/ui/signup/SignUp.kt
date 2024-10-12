package ua.rikutou.studio.ui.signup

object SignUp {
    sealed interface Event {
        data object NavigateToLogin : Event
    }

    sealed interface Action {
        data object OnRegister: Action
        data class onNameChanged(val name: String): Action
        data class onPasswordChanged(val password: String): Action
    }

    data class State(
        val name: String = "John.Doe@gmail.com",
        val password: String = "Secret1",
        val inProgress: Boolean = false,
    )
}