package ua.rikutou.studio.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.auth.AuthDataSource
import ua.rikutou.studio.data.datasource.token.TokenDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    val tokenDataSource: TokenDataSource,
    val profileDataSource: ProfileDataSource,
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
        viewModelScope.launch {
            if (tokenDataSource.token?.isNotEmpty() == true) {
                authDataSource.getMe()
                    .catch {
                        it.printStackTrace()
                    }
                    .collect {
                    when(it) {
                        is DataSourceResponse.Error<*> -> {
                            _event.emit(Splash.Event.OnNavigate(Screen.SignIn))
                        }
                        DataSourceResponse.InProgress -> {
                        }
                        is DataSourceResponse.Success -> {
                            _event.emit(Splash.Event.OnNavigate(Screen.Studio.Main))
                        }
                    }
                }
            } else {
                _event.emit(Splash.Event.OnNavigate(Screen.SignIn))
            }
        }
    }
}