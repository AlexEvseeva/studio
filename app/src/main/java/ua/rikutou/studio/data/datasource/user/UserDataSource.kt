package ua.rikutou.studio.data.datasource.user

import kotlinx.coroutines.flow.StateFlow
import ua.rikutou.studio.data.local.entity.UserEntity

interface UserDataSource {
    val userFlow: StateFlow<UserEntity?>
    suspend fun setUser(userEntity: UserEntity?)
}