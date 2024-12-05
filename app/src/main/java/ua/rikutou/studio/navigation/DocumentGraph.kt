package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.document.DocumentCreate
import ua.rikutou.studio.ui.document.DocumentCreateScreen

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
    }
}