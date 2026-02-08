package com.planes.android.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.planes.android.screens.about.AboutEntryRepository
import com.planes.android.screens.about.AboutScreen
import com.planes.android.screens.chat.ChatScreen
import com.planes.android.screens.createmultiplayergame.CreateMultiplayerGameScreen
import com.planes.android.screens.createmultiplayergame.CreateViewModel
import com.planes.android.screens.deleteuser.DeleteUserScreen
import com.planes.android.screens.login.LoginScreen
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.multiplayergame.BoardEditingScreenMultiPlayer
import com.planes.android.screens.multiplayergame.ComputerGridViewModelMultiPlayer
import com.planes.android.screens.multiplayergame.GameNotStartedScreenMultiPlayer
import com.planes.android.screens.multiplayergame.GameScreenMultiPlayer
import com.planes.android.screens.multiplayergame.GameStatsViewModelMultiPlayer
import com.planes.android.screens.multiplayergame.PlayerGridViewModelMultiPlayer
import com.planes.android.screens.multiplayergamestatistics.MultiplayerGameStatisticsScreen
import com.planes.android.screens.norobot.NoRobotScreen
import com.planes.android.screens.norobot.NoRobotViewModel
import com.planes.android.screens.register.RegisterScreen
import com.planes.android.screens.singleplayergame.BoardEditingScreenSinglePlayer
import com.planes.android.screens.singleplayergamestatistics.SinglePlayerGameStatisticsScreen
import com.planes.android.screens.preferences.PreferencesScreen
import com.planes.android.screens.preferences.PreferencesViewModel
import com.planes.android.screens.register.RegisterViewModel
import com.planes.android.screens.singleplayergame.ComputerGridViewModelSinglePlayer
import com.planes.android.screens.singleplayergame.GameNotStartedScreenSinglePlayer
import com.planes.android.screens.singleplayergame.GameScreenSinglePlayer
import com.planes.android.screens.singleplayergame.GameStatsViewModelSinglePlayer
import com.planes.android.screens.singleplayergame.PlayerGridViewModelSinglePlayer
import com.planes.android.screens.video.VideoModelRepository
import com.planes.android.screens.video.VideoScreen
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.SinglePlayerRoundInterface

@Composable
fun PlanesNavigation(modifier: Modifier, currentScreenState: MutableState<String>,
                     topBarHeight: MutableState<Int>,
                     navController: NavHostController,
                     context: Context,
                     planeRound: SinglePlayerRoundInterface,
                     planeRoundMultiplayer: MultiPlayerRoundInterface,
                     optionsViewModel: PreferencesViewModel,
                     playerGridViewModelSinglePlayer: PlayerGridViewModelSinglePlayer,
                     computerGridViewModelSinglePlayer: ComputerGridViewModelSinglePlayer,
                     gameStatsViewModelSinglePlayer: GameStatsViewModelSinglePlayer,
                     playerGridViewModelMultiPlayer: PlayerGridViewModelMultiPlayer,
                     computerGridViewModelMultiPlayer: ComputerGridViewModelMultiPlayer,
                     gameStatsViewModelMultiPlayer: GameStatsViewModelMultiPlayer,
                     loginViewModel: LoginViewModel,
                     registerViewModel: RegisterViewModel,
                     noRobotViewModel: NoRobotViewModel,
                     createViewModel: CreateViewModel
) {


    NavHost(
        navController = navController,
        startDestination = PlanesScreens.Info.name) {
        composable(PlanesScreens.SinglePlayerGame.name) {
           GameScreenSinglePlayer(modifier = modifier,
               currentScreenState, topBarHeight, navController = navController,
               planeRound,
               playerGridViewModelSinglePlayer, computerGridViewModelSinglePlayer, gameStatsViewModelSinglePlayer)
        }
        composable(PlanesScreens.SinglePlayerBoardEditing.name) {
            BoardEditingScreenSinglePlayer(modifier = modifier,
                currentScreenState, topBarHeight, navController = navController,
                planeRound, playerGridViewModelSinglePlayer)
        }
        composable(PlanesScreens.SinglePlayerGameNotStarted.name) {
            GameNotStartedScreenSinglePlayer(modifier = modifier,
                currentScreenState, topBarHeight, navController = navController,
                planeRound,
                playerGridViewModelSinglePlayer, computerGridViewModelSinglePlayer)
        }
        composable(PlanesScreens.SinglePlayerGameStatistics.name) {
            SinglePlayerGameStatisticsScreen(modifier = modifier, currentScreenState,
                navController = navController)
        }
        composable(PlanesScreens.Preferences.name) {
            PreferencesScreen(modifier = modifier, currentScreenState,
                navController = navController, optionsViewModel = optionsViewModel,
                planeRound = planeRound)
        }

        composable(PlanesScreens.CreateMultiplayerGame.name) {
            CreateMultiplayerGameScreen(modifier = modifier, currentScreenState, navController = navController,
                loginViewModel, createViewModel, planeRoundMultiplayer, playerGridViewModelMultiPlayer,
                computerGridViewModelMultiPlayer)
        }

        composable(PlanesScreens.MultiplayerBoardEditing.name) {
            BoardEditingScreenMultiPlayer(modifier = modifier,
                currentScreenState, topBarHeight, navController = navController,
                loginViewModel, createViewModel,
                planeRoundMultiplayer, playerGridViewModelMultiPlayer,
                computerGridViewModelMultiPlayer)
        }
        composable(PlanesScreens.MultiplayerGame.name) {
            GameScreenMultiPlayer(modifier = modifier,
                currentScreenState, topBarHeight, navController = navController,
                planeRoundMultiplayer,
                playerGridViewModelMultiPlayer, computerGridViewModelMultiPlayer, gameStatsViewModelMultiPlayer)
        }
        composable(PlanesScreens.MultiplayerGameNotStarted.name) {
            GameNotStartedScreenMultiPlayer(modifier = modifier,
                currentScreenState, topBarHeight, navController = navController,
                planeRoundMultiplayer,
                playerGridViewModelMultiPlayer, computerGridViewModelMultiPlayer)
        }
        composable(PlanesScreens.MultiplayerGameStatistics.name) {
            MultiplayerGameStatisticsScreen(modifier = modifier, currentScreenState, navController = navController)
        }
        composable(PlanesScreens.Info.name) {
            AboutScreen(modifier = modifier, currentScreenState, navController = navController,
                context = context,
                aboutEntryList = AboutEntryRepository.create("0.1", context = context))
        }
        composable(PlanesScreens.Tutorials.name) {
            VideoScreen(modifier = modifier, currentScreenState, navController = navController,
                videoModelList = VideoModelRepository.create(context = context))
        }
        composable(PlanesScreens.Login.name) {
            LoginScreen(modifier = modifier, currentScreenState, navController = navController,
                loginViewModel)
        }
        composable(PlanesScreens.Register.name) {
            RegisterScreen(modifier = modifier, currentScreenState, navController = navController,
                registerViewModel, noRobotViewModel)
        }
        composable(PlanesScreens.NoRobot.name) {
            NoRobotScreen(modifier = modifier, currentScreenState, navController = navController,
                noRobotViewModel)
        }
        composable(PlanesScreens.DeleteUser.name) {
            DeleteUserScreen(modifier = modifier, currentScreenState, navController = navController,
                loginViewModel)
        }
        composable(PlanesScreens.Chat.name) {
            ChatScreen(modifier = modifier, currentScreenState, navController = navController)
        }
    }
}

/*

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
 */