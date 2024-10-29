package ua.rikutou.studio.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.equipmentGraph(navController: NavController) {
    navigation<NestedGraph.Equipment>(
        startDestination = Screen.Equipment
    ) {
        composable<Screen.Equipment> {
            Text(text = "equipment in progress")
        }
    }
}