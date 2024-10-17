package ua.rikutou.studio.data.datasource.token

interface TokenDataSource {
    val token: String?
    suspend fun setToken(newToken: String?)
}