package ua.rikutou.studio.data.repository.token

interface TokenDataSource {
    val token: String?
    suspend fun setToken(newToken: String)
}