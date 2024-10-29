package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.signin.SignInScreen
import ua.rikutou.studio.ui.signup.SignUpScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation<NestedGraph.Auth>(
        startDestination = Screen.SignIn,
    ) {
        composable<Screen.SignUp> {
            SignUpScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.SignIn> {
            SignInScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}