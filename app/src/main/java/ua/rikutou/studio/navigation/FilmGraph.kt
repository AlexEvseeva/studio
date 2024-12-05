package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.film.details.FilmDetailsScreen

fun NavGraphBuilder.filmGraph(
    navController: NavController
) {
    navigation<NestedGraph.Film>(
        startDestination = Screen.Film.List
    ) {
        composable<Screen.Film.List> {  }

        composable<Screen.Film.Details> {
            FilmDetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Film.Edit> {  }

    }
}