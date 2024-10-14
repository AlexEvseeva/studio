package ua.rikutou.studio.ui.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import ua.rikutou.studio.ui.signup.SignUp

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is SignIn.Event.OnMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    SignInScreenContent(state = state, onAction = viewModel::onAction)
}

@Composable
fun SignInScreenContent(
    state : State<SignIn.State>,
    onAction: (SignIn.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        var name by remember { mutableStateOf(state.value.name) }
        var password by remember { mutableStateOf(state.value.password) }

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = name,
            onValueChange = {
                name = it
                onAction(SignIn.Action.onNameChanged(name = it))
            },
            label = {
                Text(text = "Email")
            }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
                onAction(SignIn.Action.onPasswordChanged(password = it))
            },
            label = {
                Text(text = "password")
            }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(SignIn.Action.OnLogin)
            }
        ) {
            Text(text = "Sign in")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview(modifier: Modifier = Modifier) {
    val state = remember {
        mutableStateOf(
            SignIn.State()
        )
    }
    SignInScreenContent(
        state = state,
        onAction = {}
    )

}