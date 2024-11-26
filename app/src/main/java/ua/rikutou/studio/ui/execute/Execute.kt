package ua.rikutou.studio.ui.execute

object Execute {
    sealed interface Event {
        data class OnMessage(val message: String) : Event
    }

    sealed interface Action {
        data object OnQuery : Action
        data object OnClear : Action
        data class OnQueryStringChanged(val query: String) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val columnNames: List<String> = emptyList(),
        val queryResult: List<List<String>> = emptyList(),
        val query: String = "SELECT * FROM USERS"
    )
}