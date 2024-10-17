package ua.rikutou.studio.navigation

sealed class Screen(val route: String) {
    data object SignUp : Screen("signup")
    data object SignIn : Screen("signin")
    data object Main : Screen("main")
    data object Splash : Screen("splash")
}