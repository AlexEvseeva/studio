package ua.rikutou.studio.ui.studio.edit

import ua.rikutou.studio.data.local.entity.StudioEntity

object StudioEdit {
    sealed interface Event{}
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
    }
    data class State(
        val studioId: Long? = null,
        val name: String? = null,
        val address: String? = null,
        val postIndex: String? = null,
        val site: String? = null,
        val youtube: String? = null,
        val facebook: String? = null,
    )
}