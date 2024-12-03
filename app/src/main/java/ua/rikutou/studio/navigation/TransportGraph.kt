package ua.rikutou.studio.navigation

import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.transport.details.TransportDetailsScreen
import ua.rikutou.studio.ui.transport.edit.TransportEditScreen
import ua.rikutou.studio.ui.transport.list.TransportListScreen

fun NavGraphBuilder.transportGraph(
    navController: NavController
) {
    navigation<NestedGraph.Transport>(
        startDestination = Screen.Transport.List
    ) {
        composable<Screen.Transport.List> {
            TransportListScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Transport.Details> {
            TransportDetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Transport.Edit> {
            TransportEditScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

    }
}