package ua.rikutou.studio.data.repository.auth

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.repository.RepositoryResponse

interface AuthRepository {
    suspend fun signUp(name: String, password: String): Flow<RepositoryResponse<Any>>
}