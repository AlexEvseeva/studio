package ua.rikutou.studio.data.datasource.profile

import android.util.Log
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import ua.rikutou.studio.data.local.DbDataSource
import ua.rikutou.studio.data.local.entity.UserEntity
import ua.rikutou.studio.data.remote.user.UserApi
import ua.rikutou.studio.data.remote.user.dto.toEntity
import ua.rikutou.studio.di.DbDeliveryDispatcher


class ProfileDataSourceImpl(): ProfileDataSource {

    private val TAG by lazy { ProfileDataSourceImpl::class.simpleName }

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