package ua.rikutou.studio.navigation

import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.actor.list.ActorListScreen

fun NavGraphBuilder.actorGraph(navController: NavController) {
    navigation<NestedGraph.Actor>(
        startDestination = Screen.Actor.List
    ) {
        composable<Screen.Actor.List> {
            ActorListScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Actor.Details> {

        }

        composable<Screen.Actor.Edit> {

        }
    }
}