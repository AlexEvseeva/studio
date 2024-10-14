package ua.rikutou.studio.ui.signup

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
import ua.rikutou.studio.data.repository.RepositoryResponse
import ua.rikutou.studio.data.repository.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
  private val authRepository: AuthRepository
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
        }
    }
    suspend fun register(name: String, password: String) {
        if (state.value.name.isEmpty() || state.value.password.isEmpty()) {
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
                is RepositoryResponse.Error -> {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                    it.message?.let {
                        _event.emit(SignUp.Event.OnMessage(message = it))
                    }
                }
                RepositoryResponse.InProgress -> {
                    _state.update {
                        it.copy(inProgress = true)
                    }
                }
                is RepositoryResponse.Success -> {
                    _state.update {
                        it.copy(inProgress = false)
                    }
                    _event.emit(SignUp.Event.NavigateToLogin)
                }
            }
        }
    }
}