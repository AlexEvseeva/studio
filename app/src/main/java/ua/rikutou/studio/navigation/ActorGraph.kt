package ua.rikutou.studio.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.actorGraph(navController: NavController) {
    navigation<NestedGraph.Actor>(
        startDestination = Screen.Actor
    ) {
        composable<Screen.Actor> {
            Text(text = "Actors in progress")
        }
    }
}