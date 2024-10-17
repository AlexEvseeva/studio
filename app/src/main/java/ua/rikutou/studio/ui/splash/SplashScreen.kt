package ua.rikutou.studio.ui.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is Splash.Event.OnNavigate -> {
                    navController.navigate(it.route)
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