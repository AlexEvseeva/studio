package ua.rikutou.studio.ui.profile

import ua.rikutou.studio.data.local.entity.UserEntity
import ua.rikutou.studio.navigation.Screen

object Profile {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data class OnMessage(val message: String) : Event
    }

    sealed interface Action {
        data class OnCheckedChanged(val user: UserEntity) : Action
        data object OnDeleteAccount : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val user: UserEntity? = null,
        val candidatesList: List<UserEntity> = emptyList()
    )
}