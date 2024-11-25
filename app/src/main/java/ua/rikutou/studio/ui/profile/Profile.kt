package ua.rikutou.studio.ui.profile

import ua.rikutou.studio.data.local.entity.UserEntity

object Profile {
    sealed interface Event {

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