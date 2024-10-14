package ua.rikutou.studio.ui.signin

import androidx.compose.runtime.MutableFloatState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.repository.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _event = MutableSharedFlow<SignIn.Event>()
    val event = _event.asSharedFlow()
    private val _state = MutableStateFlow(SignIn.State())
    val state = _state.asStateFlow()

    fun onAction(action: SignIn.Action) = viewModelScope.launch {
        when (action) {
            SignIn.Action.OnLogin -> TODO()
            is SignIn.Action.onNameChanged -> TODO()
            is SignIn.Action.onPasswordChanged -> TODO()
        }
    }

    suspend fun onLogin(name: String, password: String) {
        if (state.value.name.isEmpty() || state.value.password.isEmpty()) {
            return
        }
    }
}