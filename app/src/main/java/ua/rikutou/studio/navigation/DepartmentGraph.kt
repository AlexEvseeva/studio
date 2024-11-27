package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.dept.DepartmentListScreen

fun NavGraphBuilder.departmentGraph(navController: NavController) {
    navigation<NestedGraph.Department>(
        startDestination = Screen.Department.List,
    ) {
        composable<Screen.Department.List> {
            DepartmentListScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

    }
}