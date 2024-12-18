package ua.rikutou.studio.ui.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.config.passwordMinLength
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.auth.AuthDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
  private val authRepository: AuthDataSource
) : ViewModel() {
    private val _event = MutableSharedFlow<SignUp.Event>()
    val event = _event.asSharedFlow()
    private val _state = MutableStateFlow(SignUp.State())
    val state = _state.asStateFlow()

    fun onAction(action: SignUp.Action) = viewModelScope.launch {
        when (action) {
            SignUp.Action.OnRegister -> register(name = state.value.name, password = state.value.password)
            is SignUp.Action.onNameChanged -> {
                _state.update {
                    it.copy(
                        name = action.name
                    )
                }
            }
            is SignUp.Action.onPasswordChanged -> {
                _state.update {
                    it.copy(
                        password = action.password
                    )
                }
            }

            is SignUp.Action.OnNavigate -> _event.emit(SignUp.Event.OnNavigate(action.destination))
        }
    }
    suspend fun register(name: String, password: String) {
        if(state.value.name.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(state.value.name).matches()) {
            _event.emit(SignUp.Event.OnMessage(message = "Name empty or not valid email"))
            return
        }
        if (state.value.password.isEmpty()
            || state.value.password.length < passwordMinLength
            || !state.value.password.contains(Regex("""\d"""))
            || !state.value.password.contains(Regex("""[A-Z]"""))
            ) {
            _event.emit(SignUp.Event.OnMessage(message = "Password must be longer then $passwordMinLength, has Uppercase and a digit"))
            return
        }
        authRepository.signUp(
            name = name,
            password = password
        ).catch {
            _state.update {
                it.copy(inProgress = false)
            }
        }.collect {
            when(it){
                is DataSourceResponse.Error<*> -> {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                    it.message?.let {
                        _event.emit(SignUp.Event.OnMessage(message = it))
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
                    _event.emit(SignUp.Event.OnNavigate(destination = Screen.SignIn))
                }
            }
        }
    }
}