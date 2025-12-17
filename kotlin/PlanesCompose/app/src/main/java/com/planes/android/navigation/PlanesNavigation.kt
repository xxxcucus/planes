package com.planes.android.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.planes.android.screens.about.AboutEntryRepository
import com.planes.android.screens.about.AboutScreen
import com.planes.android.screens.chat.ChatScreen
import com.planes.android.screens.deleteuser.DeleteUserScreen
import com.planes.android.screens.login.LoginScreen
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.logout.LogoutScreen
import com.planes.android.screens.multiplayergame.MultiplayerGameScreen
import com.planes.android.screens.multiplayergamestatistics.MultiplayerGameStatisticsScreen
import com.planes.android.screens.register.RegisterScreen
import com.planes.android.screens.singleplayergame.BoardEditingScreen
import com.planes.android.screens.singleplayergamestatistics.SinglePlayerGameStatisticsScreen
import com.planes.android.screens.preferences.PreferencesScreen
import com.planes.android.screens.preferences.PreferencesViewModel
import com.planes.android.screens.singleplayergame.ComputerGridViewModel
import com.planes.android.screens.singleplayergame.GameNotStartedScreen
import com.planes.android.screens.singleplayergame.GameScreen
import com.planes.android.screens.singleplayergame.GameStatsViewModel
import com.planes.android.screens.singleplayergame.PlaneGridViewModel
import com.planes.android.screens.singleplayergame.PlayerGridViewModel
import com.planes.android.screens.video.VideoModelRepository
import com.planes.android.screens.video.VideoScreen
import com.planes.singleplayerengine.PlanesRoundInterface
import javax.inject.Inject

@Composable
fun PlanesNavigation(modifier: Modifier, currentScreenState: MutableState<String>,
                     topBarHeight: MutableState<Int>,
                     navController: NavHostController,
                     context: Context,
                     planeRound: PlanesRoundInterface,
                     optionsViewModel: PreferencesViewModel,
                     playerGridViewModel: PlayerGridViewModel,
                     computerGridViewModel: ComputerGridViewModel,
                     gameStatsViewModel: GameStatsViewModel,
                     loginViewModel: LoginViewModel
) {


    NavHost(
        navController = navController,
        startDestination = PlanesScreens.Info.name) {
        composable(PlanesScreens.SinglePlayerGame.name) {
           GameScreen(modifier = modifier,
               currentScreenState, topBarHeight, navController = navController,
               planeRound,
               playerGridViewModel, computerGridViewModel, gameStatsViewModel)
        }
        composable(PlanesScreens.SinglePlayerBoardEditing.name) {
            BoardEditingScreen(modifier = modifier,
                currentScreenState, topBarHeight, navController = navController,
                planeRound, playerGridViewModel)
        }
        composable(PlanesScreens.SinglePlayerGameNotStarted.name) {
            GameNotStartedScreen(modifier = modifier,
                currentScreenState, topBarHeight, navController = navController,
                planeRound,
                playerGridViewModel, computerGridViewModel)
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
        composable(PlanesScreens.MultiplayerGame.name) {
            MultiplayerGameScreen(modifier = modifier, currentScreenState, navController = navController)
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
        composable(PlanesScreens.Logout.name) {
            LogoutScreen(modifier = modifier, currentScreenState, navController = navController)
        }
        composable(PlanesScreens.Register.name) {
            RegisterScreen(modifier = modifier, currentScreenState, navController = navController)
        }
        composable(PlanesScreens.DeleteUser.name) {
            DeleteUserScreen(modifier = modifier, currentScreenState, navController = navController)
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