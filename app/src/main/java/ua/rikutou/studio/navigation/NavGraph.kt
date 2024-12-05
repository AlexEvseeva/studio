package ua.rikutou.studio.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.rikutou.studio.ui.execute.ExecuteScreen
import ua.rikutou.studio.ui.splash.SplashScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = Screen.Splash
    ) {
        composable<Screen.Splash> {
            SplashScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Execute> {
            ExecuteScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        authGraph(navController = navController)
        studioGraph(navController = navController)
        locationGraph(navController = navController)
        equipmentGraph(navController = navController)
        actorGraph(navController = navController)
        profileGraph(navController = navController)
        departmentGraph(navController = navController)
        sectionGraph(navController = navController)
        transportGraph(navController = navController)
        filmGraph(navController = navController)

    }
}