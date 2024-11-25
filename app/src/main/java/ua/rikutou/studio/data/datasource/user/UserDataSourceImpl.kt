package ua.rikutou.studio.data.datasource.user

import android.util.Log
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.UserEntity
import ua.rikutou.studio.data.remote.user.UserApi
import ua.rikutou.studio.data.remote.user.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher

class UserDataSourceImpl(
    val userApi: UserApi,
    val dbDataSource: DbDataSource,
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

    override suspend fun deleteUserById(userId: Long): Flow<DataSourceResponse<Nothing>> = flow {
    }
}