package ua.rikutou.studio.ui.main

object Main {
    sealed interface Event {

    }

    sealed interface Acton {

    }

    data class State (
        val inProgress: Boolean = false,

    )

}