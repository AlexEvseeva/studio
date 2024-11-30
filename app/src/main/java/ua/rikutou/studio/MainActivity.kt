package ua.rikutou.studio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.rikutou.studio.navigation.NavGraph
import ua.rikutou.studio.navigation.NestedGraph
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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding(),
                    bottomBar = {
                        if(showBottomNavigation(currentDestination)) {
                            BottomNavigation(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                topLevelRoutes.forEach { topLevelRoutes ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                painterResource(topLevelRoutes.icon),
                                                contentDescription = topLevelRoutes.name
                                            )
                                        },
                                        label = {
                                            Text(
                                                topLevelRoutes.name,
                                                fontSize = 8.5.sp,
                                                color = Color.White
                                            )
                                        },
                                        selected = currentDestination?.hierarchy?.any {
                                            it.hasRoute(
                                                topLevelRoutes.destination::class
                                            )
                                        } == true,
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

                    }
                ) { innerPadding ->
                        NavGraph(
                            modifier = Modifier
                                .safeContentPadding()
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
    TopLevelRoute(name = "Stud", destination = NestedGraph.Studio, icon = R.drawable.studio),
    TopLevelRoute(name = "Loc", destination = NestedGraph.Location, icon = R.drawable.location),
    TopLevelRoute(name = "Equ", destination = NestedGraph.Equipment, icon = R.drawable.camera),
    TopLevelRoute(name = "Dept", destination = NestedGraph.Department, icon = R.drawable.department),
    TopLevelRoute(name = "Act", destination = NestedGraph.Actor, icon = R.drawable.actors),
    TopLevelRoute(name = "Pro", destination = NestedGraph.Profile,icon = R.drawable.profile)
)

private fun showBottomNavigation(navDestination: NavDestination?): Boolean =
    navDestination?.let { destination ->
        destination.route in listOf(
            Screen.Studio.Main::class.qualifiedName,
            Screen.Location.List::class.qualifiedName,
            Screen.Actor::class.qualifiedName,
            Screen.Equipment::class.qualifiedName,
            Screen.Department.List::class.qualifiedName,
            Screen.Equipment.List::class.qualifiedName
        )
    } ?: false