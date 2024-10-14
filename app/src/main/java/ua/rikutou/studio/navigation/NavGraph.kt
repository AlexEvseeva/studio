package ua.rikutou.studio.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.rikutou.studio.ui.signin.SignInScreen
import ua.rikutou.studio.ui.signup.SignUpScreen
import ua.rikutou.studio.ui.signup.SignUpViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = Screen.SignUp.route
    ) {
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable(route = Screen.SignIn.route) {
            SignInScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}