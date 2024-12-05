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
    @Serializable sealed class Department : Screen() {
        @Serializable data object List : Department()
        @Serializable data class Details(val departmentId: Long) : Department()
        @Serializable data class Edit(val departmentId: Long? = null) : Department()
    }
    @Serializable sealed class Section : Screen() {
        @Serializable data object List : Section()
        @Serializable data class Details(val sectionId: Long) : Section()
        @Serializable data class Edit(val sectionId: Long? = null) : Section()
    }
    @Serializable sealed class Equipment : Screen() {
        @Serializable data object List : Equipment()
        @Serializable data class Details(val equipmentId: Long) : Equipment()
        @Serializable data class Edit(val equipmentId: Long? = null) : Equipment()
    }
    @Serializable sealed class Actor : Screen() {
        @Serializable data object List : Actor()
        @Serializable data class Details(val actorId: Long) : Actor()
        @Serializable data class Edit(val actorId: Long? = null) : Actor()
    }
    @Serializable data object Profile : Screen()
    @Serializable data object Execute : Screen()

    @Serializable sealed class Transport : Screen() {
        @Serializable data object List: Transport()
        @Serializable data class Details(val transportId: Long) : Transport()
        @Serializable data class Edit(val transportId: Long? = null) : Transport()
    }

    @Serializable sealed class Film: Screen() {
        @Serializable data object List : Film()
        @Serializable data class Details(val filmId: Long) : Film()
        @Serializable data class Edit(val filmId: Long? = null) : Film()
    }

}