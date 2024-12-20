package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.location.details.LocationDetailsScreen
import ua.rikutou.studio.ui.location.edit.LocationEditScreen
import ua.rikutou.studio.ui.location.list.LocationListScreen

fun NavGraphBuilder.locationGraph(navController: NavController) {
    navigation<NestedGraph.Location>(
        startDestination = Screen.Location.List
    ) {
        composable<Screen.Location.List> {
            LocationListScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Location.Details> {
            LocationDetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Location.Edit> {
            LocationEditScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

    }
}