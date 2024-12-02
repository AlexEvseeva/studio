package ua.rikutou.studio.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.rikutou.studio.ui.execute.ExecuteScreen
import ua.rikutou.studio.ui.location.list.LocationListScreen
import ua.rikutou.studio.ui.section.details.SectionDetailsScreen
import ua.rikutou.studio.ui.section.edit.SectionEditScreen
import ua.rikutou.studio.ui.studio.main.MainScreen
import ua.rikutou.studio.ui.signin.SignInScreen
import ua.rikutou.studio.ui.signup.SignUpScreen
import ua.rikutou.studio.ui.signup.SignUpViewModel
import ua.rikutou.studio.ui.splash.SplashScreen
import ua.rikutou.studio.ui.studio.edit.StudioEdit
import ua.rikutou.studio.ui.studio.edit.StudioEditScreen

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

        authGraph(navController = navController)

        studioGraph(navController = navController)

        locationGraph(navController = navController)

        equipmentGraph(navController = navController)

        actorGraph(navController = navController)

        profileGraph(navController = navController)

        departmentGraph(navController = navController)

        sectionGraph(navController = navController)

        composable<Screen.Execute> {
            ExecuteScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        transportGraph(navController = navController)

    }
}