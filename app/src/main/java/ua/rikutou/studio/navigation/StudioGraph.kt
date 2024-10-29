package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.studio.edit.StudioEditScreen
import ua.rikutou.studio.ui.studio.main.MainScreen

fun NavGraphBuilder.studioGraph(navController: NavController) {
    navigation<NestedGraph.Studio>(
        startDestination = Screen.Studio.Main
    ) {
        composable<Screen.Studio.Main> {
            MainScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Studio.Edit> {
            StudioEditScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}