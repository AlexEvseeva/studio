package ua.rikutou.studio.navigation

import kotlinx.serialization.Serializable
import ua.rikutou.studio.ui.location.list.LocationList

@Serializable
sealed class Screen {
    @Serializable data object SignUp : Screen()
    @Serializable data object SignIn : Screen()
    @Serializable data object Splash : Screen()

    @Serializable sealed class Studio : Screen(){
        @Serializable data object Main : Studio()
        @Serializable data class Edit(val studioId: Long?) : Studio()
    }
    @Serializable sealed class Location : Screen() {
        @Serializable data object List : Location()
        @Serializable data class Details(val locationId: Long) : Location()
        @Serializable data class Edit(val locationId: Long? = null) : Location()
    }
    @Serializable data object Equipment : Screen()
    @Serializable data object Department : Screen()
    @Serializable data object Actor : Screen()
    @Serializable data object Profile : Screen()

}