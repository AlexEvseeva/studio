package ua.rikutou.studio.data.datasource.user

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.local.entity.UserEntity


class UserDataSourceImpl(): UserDataSource {
    private val _userFlow = MutableStateFlow<UserEntity?>(null)
    private var _user: UserEntity? = null

    override suspend fun setUser(userEntity: UserEntity?) {
        _userFlow.emit(userEntity)
        _user = userEntity
    }

    override suspend fun updateStudio(studioId: Long) {
        _user = _user?.copy(studioId = studioId)
        _userFlow.emit(_user)
    }

    override val userFlow = _userFlow.asStateFlow()

    override val user: UserEntity?
        get() = _user

}