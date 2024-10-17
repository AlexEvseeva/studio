package ua.rikutou.studio.data.datasource.auth

import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.rikutou.studio.data.remote.auth.AuthApi
import ua.rikutou.studio.data.remote.auth.dto.AuthRequest
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.token.TokenDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
import ua.rikutou.studio.data.local.entity.UserEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class AuthDataSourceImpl(
    private val authApi: AuthApi,
    private val userDataSource: UserDataSource,
    private val tokenDataSource: TokenDataSource,
) : AuthDataSource {
    override suspend fun signUp(name: String, password: String): Flow<DataSourceResponse<Any>> = flow {
        emit(DataSourceResponse.InProgress)
        authApi.signUp(
            body = AuthRequest(
                name = name,
                password = password
            )
        ).run {
            when {
                isSuccessful -> {
                    emit(DataSourceResponse.Success())
                }
                409 == code() -> {
                    emit(DataSourceResponse.Error(message = "User already created"))
                }
                else -> {
                    emit(DataSourceResponse.Error())
                }
            }
        }
    }

    override suspend fun signIn(name: String, password: String): Flow<DataSourceResponse<Any>> = flow {
        emit(DataSourceResponse.InProgress)
        authApi.signIn(
            body = AuthRequest(
                name = name,
                password = password
            )
        ).run {
            when {
                isSuccessful -> {
                    body()?.let {
                        tokenDataSource.setToken(it?.token ?: "")
                        userDataSource.setUser(userEntity = UserEntity(
                            userId = it.userId ?: -1L,
                            name = it.userName ?: "",
                            studioId = it.studioId
                        ))
                    }
                }
                else -> {
                    emit(DataSourceResponse.Error())
                }
            }
        }
    }

    override suspend fun getMe(): Flow<DataSourceResponse<Any>> = flow {
        emit(DataSourceResponse.InProgress)
        authApi.getMe().run {
            when {
                isSuccessful -> {
                    body()?.let {
                        userDataSource.setUser(
                            UserEntity(
                                userId = it.userId ?: -1L,
                                name = it.userName ?: "",
                                studioId = it.studioId
                            )
                        )
                        emit(DataSourceResponse.Success())
                    }
                }
                else -> {
                    tokenDataSource.setToken(null)
                    userDataSource.setUser(null)
                    emit(DataSourceResponse.Error())
                }
            }
        }
    }
}