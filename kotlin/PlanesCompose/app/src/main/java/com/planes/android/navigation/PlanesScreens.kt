package com.planes.android.navigation

enum class PlanesScreens {
    SinglePlayerGame,
    SinglePlayerPreferences,
    SinglePlayerGameStatistics,
    MultiplayerGame,
    MultiplayerPreferences,
    MultiplayerGameStatistics,
    MultiplayerConnectToGame,
    Info,
    Tutorials,
    Login,
    Logout,
    Register,
    DeleteUser;
    companion object {
        fun fromRoute(route: String?) : PlanesScreens  =
            when(route?.substringBefore("/")) {
                Info.name -> Info
                Tutorials.name -> Tutorials
                SinglePlayerGame.name -> SinglePlayerGame
                SinglePlayerPreferences.name -> SinglePlayerPreferences
                SinglePlayerGameStatistics.name -> SinglePlayerGameStatistics
                MultiplayerGame.name -> MultiplayerGame
                MultiplayerPreferences.name -> MultiplayerPreferences
                MultiplayerGameStatistics.name -> MultiplayerGameStatistics
                MultiplayerConnectToGame.name -> MultiplayerConnectToGame
                Login.name -> Login
                Logout.name -> Logout
                Register.name -> Register
                DeleteUser.name -> DeleteUser
                null -> Info
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}