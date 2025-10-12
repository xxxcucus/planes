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
import com.planes.android.about.AboutEntryRepository
import com.planes.android.about.AboutScreen
import com.planes.android.chat.ChatScreen
import com.planes.android.deleteuser.DeleteUserScreen
import com.planes.android.login.LoginScreen
import com.planes.android.logout.LogoutScreen
import com.planes.android.multiplayergame.MultiplayerGameScreen
import com.planes.android.multiplayergamestatistics.MultiplayerGameStatisticsScreen
import com.planes.android.register.RegisterScreen
import com.planes.android.singleplayergame.BoardEditingScreen
import com.planes.android.singleplayergamestatistics.SinglePlayerGameStatisticsScreen
import com.planes.android.preferences.PreferencesScreen
import com.planes.android.preferences.PreferencesViewModel
import com.planes.android.singleplayergame.ComputerGridViewModel
import com.planes.android.singleplayergame.GameNotStartedScreen
import com.planes.android.singleplayergame.GameScreen
import com.planes.android.singleplayergame.PlaneGridViewModel
import com.planes.android.singleplayergame.PlayerGridViewModel
import com.planes.android.video.VideoModelRepository
import com.planes.android.video.VideoScreen
import com.planes.singleplayerengine.PlanesRoundInterface
import javax.inject.Inject

@Composable
fun PlanesNavigation(modifier: Modifier, currentScreenState: MutableState<String>,
                     topBarHeight: MutableState<Int>,
                     navController: NavHostController,
                     context: Context,
                     optionsViewModel: PreferencesViewModel,
                     playerGridViewModel: PlayerGridViewModel,
                     computerGridViewModel: ComputerGridViewModel) {


    NavHost(
        navController = navController,
        startDestination = PlanesScreens.Info.name) {
        composable(PlanesScreens.SinglePlayerGame.name) {
           GameScreen(modifier = modifier, currentScreenState, topBarHeight, navController = navController, playerGridViewModel, computerGridViewModel)
        }
        composable(PlanesScreens.SinglePlayerBoardEditing.name) {
            BoardEditingScreen(modifier = modifier, currentScreenState, topBarHeight, navController = navController, playerGridViewModel)
        }
        composable(PlanesScreens.SinglePlayerGameNotStarted.name) {
            GameNotStartedScreen(modifier = modifier, currentScreenState, topBarHeight, navController = navController, playerGridViewModel, computerGridViewModel)
        }
        composable(PlanesScreens.SinglePlayerGameStatistics.name) {
            SinglePlayerGameStatisticsScreen(modifier = modifier, currentScreenState, navController = navController)
        }
        composable(PlanesScreens.Preferences.name) {
            PreferencesScreen(modifier = modifier, currentScreenState, navController = navController, optionsViewModel = optionsViewModel)
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
            LoginScreen(modifier = modifier, currentScreenState, navController = navController)
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