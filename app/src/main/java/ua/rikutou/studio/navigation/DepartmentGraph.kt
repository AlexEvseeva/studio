package ua.rikutou.studio.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.dept.details.DepartmentDetailsScreen
import ua.rikutou.studio.ui.dept.list.DepartmentListScreen

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
        composable<Screen.Department.Details> {
            DepartmentDetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

    }
}