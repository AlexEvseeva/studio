package ua.rikutou.studio.ui.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    SignUpScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun SignUpScreenContent(
    state: State<SignUp.State>,
    onAction: (SignUp.Action) -> Unit
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
                onAction(SignUp.Action.onNameChanged(name = it))
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
                onAction(SignUp.Action.onPasswordChanged(password = it))
            },
            label = {
                Text(text = "password")
            }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(SignUp.Action.OnRegister)
            }
        ) {
            Text(text = "Sign up")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview(modifier: Modifier = Modifier) {
    val state = remember {
        mutableStateOf(
            SignUp.State(
                name = "",
                password = ""
            )
        )
    }
    SignUpScreenContent(
        state = state,
        onAction = {}
    )
}