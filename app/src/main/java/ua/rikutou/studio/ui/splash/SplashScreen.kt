package ua.rikutou.studio.ui.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle(initialValue = Splash.State())
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is Splash.Event.OnNavigate -> {
                    navController.navigate(it.destination)
                }
            }
        }
    }
    SplashScreenContent()
}

@Composable
fun SplashScreenContent() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}