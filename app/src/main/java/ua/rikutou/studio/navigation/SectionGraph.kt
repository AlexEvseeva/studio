package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.section.details.SectionDetails
import ua.rikutou.studio.ui.section.details.SectionDetailsScreen
import ua.rikutou.studio.ui.section.edit.SectionEditScreen

fun NavGraphBuilder.sectionGraph(
    navController: NavController
) {
    navigation<NestedGraph.Section>(
        startDestination = Screen.Section.List
    ) {
        composable<Screen.Section.List> {  }

        composable<Screen.Section.Details> {
            SectionDetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Section.Edit> {
            SectionEditScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}