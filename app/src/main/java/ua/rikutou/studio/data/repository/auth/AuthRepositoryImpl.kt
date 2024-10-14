package ua.rikutou.studio.data.repository.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.remote.auth.dto.AuthRequest
import ua.rikutou.studio.data.repository.RepositoryResponse

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun signUp(name: String, password: String): Flow<RepositoryResponse<Any>> = flow {
        emit(RepositoryResponse.InProgress)
        authApi.signUp(
            body = AuthRequest(
                name = name,
                password = password
            )
        ).run {
            when {
                isSuccessful -> {
                    emit(RepositoryResponse.Success())
                }
                409 == code() -> {
                    emit(RepositoryResponse.Error(message = "User already created"))
                }
                else -> {
                    emit(RepositoryResponse.Error())
                }
            }
        }
    }
}