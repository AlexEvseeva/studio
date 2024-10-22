package ua.rikutou.studio.ui.main

import ua.rikutou.studio.data.local.entity.StudioEntity

object Main {
    sealed interface Event {

    }

    sealed interface Acton {

    }

    data class State (
        val inProgress: Boolean = false,
        val studio: StudioEntity? = null
    )

}