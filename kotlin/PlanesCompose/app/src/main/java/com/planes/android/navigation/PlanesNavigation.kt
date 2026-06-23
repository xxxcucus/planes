package com.planes.android.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.planes.android.screens.about.AboutEntryRepository
import com.planes.android.screens.about.AboutScreen
import com.planes.android.screens.chat.ChatScreen
import com.planes.android.screens.chat.ChatUserListViewModel
import com.planes.android.screens.conversation.ConversationScreen
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
import com.planes.android.screens.splash.SplashScreen
import com.planes.android.screens.video.VideoScreen
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.SinglePlayerRoundInterface


@Composable
fun PlanesNavigation(modifier: Modifier, currentTitleState: MutableState<String>,
                     currentScreenState: MutableState<String>,
                     showPopupState: MutableState<Boolean>,
                     newMessagesState: MutableState<Boolean>,
                     userLoggedInState: MutableState<Boolean>,
                     splashScreenState: MutableState<Boolean>,
                     navController: NavHostController,
                     context: Context,
                     planeRound: SinglePlayerRoundInterface,
                     planeRoundMultiplayer: MultiPlayerRoundInterface
) {

    val playerGridViewModelSinglePlayer: PlayerGridViewModelSinglePlayer = hiltViewModel()
    val computerGridViewModelSinglePlayer: ComputerGridViewModelSinglePlayer = hiltViewModel()
    val gameStatsViewModelSinglePlayer: GameStatsViewModelSinglePlayer = hiltViewModel()
    val playerGridViewModelMultiPlayer: PlayerGridViewModelMultiPlayer = hiltViewModel()
    val computerGridViewModelMultiPlayer: ComputerGridViewModelMultiPlayer = hiltViewModel()
    val gameStatsViewModelMultiPlayer: GameStatsViewModelMultiPlayer = hiltViewModel()
    val optionsViewModel: PreferencesViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val noRobotViewModel: NoRobotViewModel = hiltViewModel()
    val createViewModel: CreateViewModel = hiltViewModel()
    val chatUserListViewModel: ChatUserListViewModel = hiltViewModel()

    //TODO: to use viewmodels local to the screens

    val messagesFlags = chatUserListViewModel.getNewMessagesFlags().collectAsStateWithLifecycle().value
    if (!loginViewModel.isLoggedIn()) {
        newMessagesState.value = false
    } else {
        newMessagesState.value =
        messagesFlags.firstOrNull {
            it.m_ReceiverId == loginViewModel.getLoggedInUserId()?.toInt()!! && it.m_NewMessages
        } != null
    }

    NavHost(
        navController = navController,
        startDestination = PlanesScreens.SplashScreen.name) {

        composable(PlanesScreens.SplashScreen.name) {
            SplashScreen(navController = navController, splashScreenState, optionsViewModel)
        }

        composable(PlanesScreens.SinglePlayerGame.name) {
           GameScreenSinglePlayer(modifier = modifier,
               currentTitleState, currentScreenState, showPopupState,
               navController = navController,
               planeRound,
               playerGridViewModelSinglePlayer, computerGridViewModelSinglePlayer, gameStatsViewModelSinglePlayer)
        }
        composable(PlanesScreens.SinglePlayerBoardEditing.name) {
            BoardEditingScreenSinglePlayer(modifier = modifier,
                currentTitleState, currentScreenState, showPopupState,
                navController = navController,
                planeRound, playerGridViewModelSinglePlayer)
        }
        composable(PlanesScreens.SinglePlayerGameNotStarted.name) {
            GameNotStartedScreenSinglePlayer(modifier = modifier,
                currentTitleState, currentScreenState, showPopupState,
                navController = navController,
                planeRound,
                playerGridViewModelSinglePlayer, computerGridViewModelSinglePlayer)
        }
        composable(PlanesScreens.SinglePlayerGameStatistics.name) {
            SinglePlayerGameStatisticsScreen(modifier = modifier, currentTitleState,
                currentScreenState,
                showPopupState, navController = navController)
        }
        composable(PlanesScreens.Preferences.name) {
            PreferencesScreen(modifier = modifier, currentTitleState,
                currentScreenState, showPopupState,
                navController = navController, optionsViewModel = optionsViewModel,
                planeRound = planeRound)
        }

        composable(PlanesScreens.CreateMultiplayerGame.name) {
            CreateMultiplayerGameScreen(modifier = modifier, currentTitleState,
                currentScreenState, showPopupState, navController = navController,
                loginViewModel, createViewModel, planeRoundMultiplayer, playerGridViewModelMultiPlayer,
                computerGridViewModelMultiPlayer)
        }

        composable(PlanesScreens.MultiplayerBoardEditing.name) {
            BoardEditingScreenMultiPlayer(modifier = modifier,
                currentTitleState, currentScreenState, showPopupState,
                navController = navController,
                loginViewModel, createViewModel,
                planeRoundMultiplayer, playerGridViewModelMultiPlayer,
                computerGridViewModelMultiPlayer)
        }
        composable(PlanesScreens.MultiplayerGame.name) {
            GameScreenMultiPlayer(modifier = modifier,
                currentTitleState, currentScreenState, showPopupState,
                navController = navController,
                planeRoundMultiplayer,
                playerGridViewModelMultiPlayer, computerGridViewModelMultiPlayer, gameStatsViewModelMultiPlayer)
        }
        composable(PlanesScreens.MultiplayerGameNotStarted.name) {
            GameNotStartedScreenMultiPlayer(modifier = modifier,
                currentTitleState, currentScreenState, showPopupState,
                navController = navController,
                planeRoundMultiplayer,
                playerGridViewModelMultiPlayer, computerGridViewModelMultiPlayer)
        }
        composable(PlanesScreens.MultiplayerGameStatistics.name) {
            MultiplayerGameStatisticsScreen(modifier = modifier, currentTitleState,
                currentScreenState, showPopupState, navController = navController)
        }
        composable(PlanesScreens.Info.name) {
            AboutScreen(modifier = modifier, currentTitleState, currentScreenState,
                showPopupState,
                navController = navController,
                context = context,
                aboutEntryList = AboutEntryRepository.create("0.1", context = context))
        }
        composable("${PlanesScreens.Tutorials.name}/{videoId}/{time}",
            arguments = listOf(
                navArgument("videoId") { type = NavType.IntType },
                navArgument("time") { type = NavType.IntType })) { entry ->

            val videoId = entry.arguments?.getInt("videoId")!!
            val time = entry.arguments?.getInt("time")!!
            VideoScreen(modifier = modifier, currentTitleState, currentScreenState,
                showPopupState, videoId, time, navController = navController)
            //PlayerRoute(modifier = modifier)
        }
        composable("${PlanesScreens.Login.name}/{autologin}",
            arguments = listOf(
                navArgument("autologin") { type = NavType.BoolType })) { entry ->

            val autologin = entry.arguments?.getBoolean("autologin")!!

            LoginScreen(modifier = modifier, currentTitleState, currentScreenState,
                showPopupState, userLoggedInState, navController = navController,
                loginViewModel, chatUserListViewModel, optionsViewModel,
                createViewModel, planeRoundMultiplayer, autologin)
        }
        composable(PlanesScreens.Register.name) {
            RegisterScreen(modifier = modifier, currentTitleState, currentScreenState,
                showPopupState,
                navController = navController,
                registerViewModel, noRobotViewModel)
        }
        composable(PlanesScreens.NoRobot.name) {
            NoRobotScreen(modifier = modifier, currentTitleState, currentScreenState,
                showPopupState,
                navController = navController,
                noRobotViewModel)
        }
        composable(PlanesScreens.DeleteUser.name) {
            DeleteUserScreen(modifier = modifier, currentTitleState, currentScreenState,
                showPopupState,
                navController = navController,
                loginViewModel)
        }
        composable(route = PlanesScreens.Chat.name) {
            ChatScreen(modifier = modifier, currentTitleState, currentScreenState,
                showPopupState,
                navController = navController,
                loginViewModel, chatUserListViewModel)
        }
        composable(route = "${PlanesScreens.Conversation.name}/{userId}/{username}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("username") { type = NavType.StringType })) { entry ->

            val userId = entry.arguments?.getString("userId")!!
            val username = entry.arguments?.getString("username")!!

            ConversationScreen(modifier = modifier, currentTitleState,
                currentScreenState,
                showPopupState,
                navController = navController,
                userId, username,
                loginViewModel)
        }
    }
}
