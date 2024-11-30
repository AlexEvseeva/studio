package ua.rikutou.studio.data.datasource.user

import kotlinx.coroutines.flow.Flow
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.local.entity.UserEntity

interface UserDataSource {
    suspend fun getStudioUsersAndCandidates(studioId: Long): Flow<List<UserEntity>>
    suspend fun loadStudioUsersAndCandidates(studioId: Long)
    suspend fun deleteUserById(userId: Long): Flow<DataSourceResponse<Nothing>>
    suspend fun updateUserStudio(user: UserEntity)
    suspend fun clearDb()
}