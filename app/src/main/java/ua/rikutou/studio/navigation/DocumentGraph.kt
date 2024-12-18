package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.document.lease.DocumentCreateScreen
import ua.rikutou.studio.ui.document.locations_report.LocationReportScreen

fun NavGraphBuilder.documentGraph(
    navController: NavController
) {
    navigation<NestedGraph.Document>(startDestination = Screen.Document.Create) {

        composable<Screen.Document.Create> {
            DocumentCreateScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Document.LocationReport> {
            LocationReportScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}