package ua.rikutou.studio.ui.studio.edit

object StudioEdit {
    sealed interface Event{}
    sealed interface Action{}
    data class State(
        val studioId: Long? = null
    )
}