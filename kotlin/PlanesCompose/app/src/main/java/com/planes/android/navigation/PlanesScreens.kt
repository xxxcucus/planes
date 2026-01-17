package com.planes.android.navigation

enum class PlanesScreens {
    SinglePlayerBoardEditing,
    SinglePlayerGame,
    SinglePlayerGameNotStarted,
    SinglePlayerGameStatistics,
    CreateMultiplayerGame,
    MultiplayerBoardEditing,
    MultiplayerGame,
    MultiplayerGameNotStarted,
    MultiplayerGameStatistics,
    MultiplayerConnectToGame,
    Info,
    Tutorials,
    Login,
    Register,
    NoRobot,
    DeleteUser,
    Chat,
    Preferences;
    companion object {
        fun fromRoute(route: String?) : PlanesScreens  =
            when(route?.substringBefore("/")) {
                Info.name -> Info
                Tutorials.name -> Tutorials
                SinglePlayerGame.name -> SinglePlayerGame
                SinglePlayerBoardEditing.name -> SinglePlayerBoardEditing
                SinglePlayerGameNotStarted.name -> SinglePlayerGameNotStarted
                Preferences.name -> Preferences
                SinglePlayerGameStatistics.name -> SinglePlayerGameStatistics
                CreateMultiplayerGame.name -> CreateMultiplayerGame
                MultiplayerBoardEditing.name -> MultiplayerBoardEditing
                MultiplayerGame.name -> MultiplayerGame
                MultiplayerGameNotStarted.name -> MultiplayerGameNotStarted
                MultiplayerGameStatistics.name -> MultiplayerGameStatistics
                MultiplayerConnectToGame.name -> MultiplayerConnectToGame
                Login.name -> Login
                Register.name -> Register
                DeleteUser.name -> DeleteUser
                Chat.name -> Chat
                null -> Info
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}