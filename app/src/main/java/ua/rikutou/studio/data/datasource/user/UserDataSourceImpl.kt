package ua.rikutou.studio.data.datasource.user

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.token.TokenDataSource
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.UserEntity
import ua.rikutou.studio.data.remote.user.UserApi
import ua.rikutou.studio.data.remote.user.dto.StudioUserDto
import ua.rikutou.studio.data.remote.user.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class UserDataSourceImpl(
    val userApi: UserApi,
    val dbDataSource: DbDataSource,
    val tokenDataSource: TokenDataSource,
    @DbDeliveryDispatcher val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : UserDataSource {
    private val TAG by lazy { UserDataSourceImpl::class.simpleName }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getStudioUsersAndCandidates(studioId: Long): Flow<List<UserEntity>> =
        dbDataSource.dbFlow.flatMapLatest { db ->
            db.userDao.getAll()
        }
            .flowOn(dbDeliveryDispatcher)
            .catch {
                it.printStackTrace()
            }

    override suspend fun loadStudioUsersAndCandidates(studioId: Long) {
        userApi.getStudioUsers(studioId = studioId).run {
            when {
                isSuccessful -> {
                    dbDataSource.db.userDao.insert(
                        body()?.map { it.toEntity() } ?: emptyList()
                    )
                }
                else -> {
                    Log.e(TAG, "loadStudioUsersAndCandidates: studioId: $studioId")
                }
            }
        }
    }

    override suspend fun deleteUserById(userId: Long): Flow<DataSourceResponse<Nothing>> = flow<DataSourceResponse<Nothing>> {
        emit(DataSourceResponse.InProgress)
        userApi.deleteUserById(
            userId = userId
        ).run {
            when {
                isSuccessful -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        dbDataSource.db.clearAllTables()
                        dbDataSource.db.close()
                        tokenDataSource.setToken(null)
                    }
                    emit(DataSourceResponse.Success())
                }
                else -> {
                    emit(DataSourceResponse.Error<Nothing>())
                }
            }
        }
    }.catch {
        CoroutineScope(Dispatchers.IO).launch {
            dbDataSource.db.clearAllTables()
            dbDataSource.db.close()
            tokenDataSource.setToken(null)
        }
        it.printStackTrace()
        emit(DataSourceResponse.Success())
    }

    override suspend fun updateUserStudio(user: UserEntity) {
        userApi.updateUserStudio(
            body = StudioUserDto(
                userId = user.userId,
                userName = user.name,
                studioId = user.studioId ?: -1L
            )
        ).run { 
            when {
                isSuccessful -> {
                    body()?.toEntity()?.let { 
                        dbDataSource.db.userDao.insert(
                            listOf(it)
                        )
                    }
                }    
                else -> {
                    Log.d(TAG, "updateUserStudio: Error: ${user.userId}")
                }
            }
            
        }

    }
}