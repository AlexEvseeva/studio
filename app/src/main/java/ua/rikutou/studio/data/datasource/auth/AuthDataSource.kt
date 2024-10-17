package ua.rikutou.studio.data.datasource.auth

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.datasource.DataSourceResponse

interface AuthDataSource {
    suspend fun signUp(name: String, password: String): Flow<DataSourceResponse<Any>>
    suspend fun signIn(name: String, password: String): Flow<DataSourceResponse<Any>>
    suspend fun getMe(): Flow<DataSourceResponse<Any>>
}