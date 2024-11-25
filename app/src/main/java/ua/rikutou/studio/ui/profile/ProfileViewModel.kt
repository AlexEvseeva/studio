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
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
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

            }
            Profile.Action.OnDeleteAccount -> {
                userDataSource.loadStudioUsersAndCandidates()
            }
        }
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