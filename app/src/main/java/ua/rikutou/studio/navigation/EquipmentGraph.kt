package ua.rikutou.studio.navigation

import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.rikutou.studio.ui.equipment.details.EquipmentDetailsScreen
import ua.rikutou.studio.ui.equipment.list.EquipmentListScreen

fun NavGraphBuilder.equipmentGraph(navController: NavController) {
    navigation<NestedGraph.Equipment>(
        startDestination = Screen.Equipment.List
    ) {
        composable<Screen.Equipment.List> {
            EquipmentListScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Equipment.Details> {
            EquipmentDetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Equipment.Edit> {
            Text(
                text = "Equipment edit screen under development"
            )
        }
    }
}