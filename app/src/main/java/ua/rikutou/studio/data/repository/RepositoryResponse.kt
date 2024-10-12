package ua.rikutou.studio.data.repository

sealed interface RepositoryResponse<out T: Any> {
    data class Error(
        val message: String? = null
    ) : RepositoryResponse<Nothing>

    data object InProgress: RepositoryResponse<Nothing>
    data class Success<out T: Any>(val payload: T? = null): RepositoryResponse<T>
}