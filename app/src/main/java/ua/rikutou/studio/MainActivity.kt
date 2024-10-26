package ua.rikutou.studio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.rikutou.studio.navigation.NavGraph
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.theme.StudioTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudioTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier
                    .fillMaxSize(),
                    bottomBar = {
                        BottomNavigation(
                            modifier = Modifier.padding(bottom = 40.dp)
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            topLevelRoutes.forEach { topLevelRoutes ->
                                BottomNavigationItem(
                                    icon = { Icon(painterResource(topLevelRoutes.icon), contentDescription = topLevelRoutes.name) },
                                    label = {
                                        Text(
                                            topLevelRoutes.name,
                                            fontSize = 12.sp,
                                            color = Color.White
                                        )
                                    },
                                    selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoutes.destination::class) } == true,
                                    onClick = {
                                        navController.navigate(topLevelRoutes.destination) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        modifier = Modifier
                            .padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}


data class TopLevelRoute<T: Any> (
    val name: String,
    val destination: T,
    @DrawableRes val icon: Int,
)

val topLevelRoutes = listOf(
    TopLevelRoute(name = "Studio", destination = Screen.Studio.Main, icon = R.drawable.studio),
    TopLevelRoute(name = "Locations", destination = Screen.Location.List, icon = R.drawable.location),
    TopLevelRoute(name = "Equipment", destination = Screen.Equipment, icon = R.drawable.camera),
    TopLevelRoute(name = "Actors", destination = Screen.Actor, icon = R.drawable.actors),
)