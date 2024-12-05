package ua.rikutou.studio.ui.signin

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.config.passwordMinLength
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.auth.AuthDataSource
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.signup.SignUp
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val authRepository: AuthDataSource
) : ViewModel() {
    private val TAG by lazy { SignInViewModel::class.simpleName }
    private val _event = MutableSharedFlow<SignIn.Event>()
    val event = _event.asSharedFlow()
    private val _state = MutableStateFlow(SignIn.State())
    val state = _state.asStateFlow()

    fun onAction(action: SignIn.Action) = viewModelScope.launch {
        when (action) {
            SignIn.Action.OnLogin -> onLogin(name = state.value.name, password = state.value.password)
            is SignIn.Action.onNameChanged -> {
               _state.update {
                   it.copy(name = action.name)
               }
            }
            is SignIn.Action.onPasswordChanged -> {
                _state.update {
                    it.copy(password = action.password)
                }
            }
            is SignIn.Action.OnNavigate -> _event.emit(SignIn.Event.OnNavigate(action.destination))
        }
    }

    suspend fun onLogin(name: String, password: String) {
        if(state.value.name.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(state.value.name).matches()) {
            _event.emit(SignIn.Event.OnMessage(message = "Name empty or not valid email"))
            return
        }
        if (state.value.password.isEmpty()
            || state.value.password.length < passwordMinLength
            || !state.value.password.contains(Regex("""\d"""))
            || !state.value.password.contains(Regex("""[A-Z]"""))
        ) {
            _event.emit(SignIn.Event.OnMessage(message = "Password must be longer then $passwordMinLength, has Uppercase and digits"))
            return
        }
        authRepository.signIn(name = name, password = password).collect {
            when (it) {
                is DataSourceResponse.Error<*> ->{
                    _state.update {
                        it.copy(inProgress = false)
                    }
                    it.message?.let {
                        _event.emit(SignIn.Event.OnMessage(it))
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
                    _event.emit(SignIn.Event.OnNavigate(Screen.Studio.Main))
                }
            }
        }
    }
}