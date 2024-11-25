package ua.rikutou.studio.navigation

import android.transition.Scene
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.profile.ProfileScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation<NestedGraph.Profile>(
        startDestination = Screen.Profile
    ) {
        composable<Screen.Profile> {
            ProfileScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}