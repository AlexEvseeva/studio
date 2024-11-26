package ua.rikutou.studio.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
import ua.rikutou.studio.data.local.entity.UserEntity
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val userDataSource: UserDataSource
) : ViewModel() {
    private val TAG by lazy { ProfileViewModel::class.simpleName }

    private val _state = MutableStateFlow(Profile.State())
    val state = _state
        .onStart {
            loadUser()
            loadCandidates()
            observCandidats()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    private val _event = MutableSharedFlow<Profile.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: Profile.Action) = viewModelScope.launch {
        when(action) {
            is Profile.Action.OnCheckedChanged -> {
                onChangeUserStudio(user = action.user)
            }
            Profile.Action.OnDeleteAccount -> {
                deleteAccount()
            }

            Profile.Action.OnExecute -> {
                _event.emit(Profile.Event.OnNavigate(route = Screen.Execute))
            }
        }
    }

    private fun deleteAccount() = viewModelScope.launch {
        val userId = state.value.user?.userId
        val studioId = state.value.user?.studioId
        val isOtherStudioOwnerPresent = studioId != null && state.value.candidatesList
            .filter { it.studioId != null &&  it.studioId > 0 }
            .any { it.studioId == studioId }

        if(userId != null && isOtherStudioOwnerPresent) {
            userDataSource.deleteUserById(userId = userId).collect {
                when(it) {
                    is DataSourceResponse.Error<*> -> {
                        _state.update {
                            it.copy(inProgress = false)
                        }
                    }
                    DataSourceResponse.InProgress -> {
                        _state.update {
                            it.copy(inProgress = true)
                        }
                    }
                    is DataSourceResponse.Success -> {
                        _state.update {
                            it.copy(inProgress = false)
                        }
                        _event.emit(Profile.Event.OnNavigate(route = Screen.SignIn))
                    }
                }
            }
        } else {
            _event.emit(
                Profile.Event.OnMessage(
                    message = "Please transfer yours right first"
                )
            )
        }
    }

    private fun onChangeUserStudio(user: UserEntity) = viewModelScope.launch {
        userDataSource.updateUserStudio(
            user = user.copy(
                studioId = if(user.studioId != null && user.studioId > 0) -1 else state.value.user?.studioId ?: -1L

            )
        )
    }

    private fun loadUser() {
        profileDataSource.user?.let { user ->
            _state.update {
                it.copy(
                    user = user
                )
            }
        }
    }

    private fun observCandidats() = viewModelScope.launch {
        profileDataSource.user?.studioId?.let { studioId ->
            userDataSource.getStudioUsersAndCandidates(studioId = studioId).collect { candidates ->
                _state.update {
                    it.copy(
                        candidatesList = candidates
                            .filter {
                                it.userId != _state.value.user?.userId
                            }
                    )
                }
            }
        }
    }

    private fun loadCandidates() = viewModelScope.launch {
        profileDataSource.user?.studioId?.let { studioId ->
            userDataSource.loadStudioUsersAndCandidates(studioId = studioId)
        }
    }
}