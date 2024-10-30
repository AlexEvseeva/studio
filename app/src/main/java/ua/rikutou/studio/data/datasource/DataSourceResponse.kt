package ua.rikutou.studio.data.datasource

sealed interface DataSourceResponse<out T: Any> {
    data class Error<out T: Any>(
        val message: String? = null
    ) : DataSourceResponse<Nothing>

    data object InProgress: DataSourceResponse<Nothing>
    data class Success<out T: Any>(val payload: T? = null): DataSourceResponse<T>
}