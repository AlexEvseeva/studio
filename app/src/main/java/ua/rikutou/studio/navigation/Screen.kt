package ua.rikutou.studio.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable data object SignUp : Screen()
    @Serializable data object SignIn : Screen()
    @Serializable data object Splash : Screen()
    @Serializable sealed class Studio : Screen(){
        @Serializable data object Main : Studio()
        @Serializable data class Edit(val studioId: Long?) : Studio()
    }
}