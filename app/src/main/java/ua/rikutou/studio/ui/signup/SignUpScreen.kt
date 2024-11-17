package ua.rikutou.studio.ui.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.signin.SignIn

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is SignUp.Event.OnMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }

                is SignUp.Event.OnNavigate -> navController.navigate(it.destination)
            }
        }
    }
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
            .fillMaxSize()
            .padding(horizontal = 4.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        var name by remember { mutableStateOf(state.value.name) }
        var password by remember { mutableStateOf(state.value.password) }

        OutlinedTextField(
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
        OutlinedTextField(
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = {
                onAction(SignUp.Action.OnRegister)
            }) {
            Text(text = "Sign up")
        }
        Text(
            modifier = Modifier
                .clickable {
                    onAction(SignUp.Action.OnNavigate(Screen.SignIn))
                }
                .padding(top = 16.dp),
            text = "Sign in",
            style = TextStyle(textDecoration = TextDecoration.Underline)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpScreenPreview(modifier: Modifier = Modifier) {
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