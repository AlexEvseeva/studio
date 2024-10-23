package ua.rikutou.studio.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.auth.AuthDataSource
import ua.rikutou.studio.data.datasource.token.TokenDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    val tokenDataSource: TokenDataSource,
    val userDataSource: UserDataSource,
    val authDataSource: AuthDataSource
) : ViewModel() {
    private val TAG by lazy { SplashViewModel::class.simpleName }
    private val _state = MutableStateFlow(Splash.State())
    val state = _state.asStateFlow().onStart {
        checkToken()
    }
    private val _event = MutableSharedFlow<Splash.Event>()
    val event = _event.asSharedFlow()

    private fun checkToken() {
//        Log.d(TAG, "get me: ${tokenDataSource.token}")
        viewModelScope.launch {
            if (tokenDataSource.token?.isNotEmpty() == true) {
                Log.d(TAG, "get me: ")
                authDataSource.getMe().collect {
                    when(it) {
                        is DataSourceResponse.Error -> {
                            Log.d(TAG, "get me: error")
                            _event.emit(Splash.Event.OnNavigate(Screen.SignIn))
                        }
                        DataSourceResponse.InProgress -> {
                            Log.d(TAG, "get me: in progress")
                        }
                        is DataSourceResponse.Success -> {
                            Log.d(TAG, "get me: success")
                            _event.emit(Splash.Event.OnNavigate(Screen.Studio.Main))
                        }
                    }
                }
            } else {
                Log.d(TAG, "else: ")
                _event.emit(Splash.Event.OnNavigate(Screen.SignIn))
            }
        }
    }
}