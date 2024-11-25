package ua.rikutou.studio.data.datasource.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ua.rikutou.studio.data.local.entity.UserEntity

interface ProfileDataSource {
    val userFlow: StateFlow<UserEntity?>
    val user: UserEntity?
    suspend fun setUser(userEntity: UserEntity?)
    suspend fun updateStudio(studioId: Long)
}