package ua.rikutou.studio.ui.studio.edit

import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.navigation.Screen

object StudioEdit {
    sealed interface Event{
        data object OnBack : Event
        data class OnNavigate(val destination: Screen) : Event
        data class OnMessage(val message: String) : Event
    }

    sealed interface Action{
        data class OnFieldchanged(
            val name: String? = null,
            val address: String? = null,
            val postIndex: String? = null,
            val site: String? = null,
            val youtube: String? = null,
            val facebook: String? = null
        ) : Action
        data object OnSave : Action
        data object OnDelete : Action
    }
    data class State(
        val inProgress: Boolean = false,
        val studioId: Long? = null,
        val name: String? = null,
        val address: String? = null,
        val postIndex: String? = null,
        val site: String? = null,
        val youtube: String? = null,
        val facebook: String? = null,
    )
}