package ua.rikutou.studio.data.remote.execute.dto

data class ExecuteResponse(
    val columns: List<String>,
    val queryResult: List<List<String>>
)
