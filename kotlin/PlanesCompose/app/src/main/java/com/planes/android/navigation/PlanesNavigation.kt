package com.planes.android.navigation

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.planes.android.about.AboutEntryModel
import com.planes.android.about.AboutEntryRepository
import com.planes.android.about.AboutScreen
import com.planes.android.chat.ChatScreen
import com.planes.android.deleteuser.DeleteUserScreen
import com.planes.android.login.LoginScreen
import com.planes.android.logout.LogoutScreen
import com.planes.android.multiplayergame.MultiplayerGameScreen
import com.planes.android.multiplayergamestatistics.MultiplayerGameStatisticsScreen
import com.planes.android.multiplayerpreferences.MultiplayerPreferencesScreen
import com.planes.android.register.RegisterScreen
import com.planes.android.singleplayergame.SinglePlayerGameScreen
import com.planes.android.singleplayergamestatistics.SinglePlayerGameStatisticsScreen
import com.planes.android.singleplayerpreferences.SinglePlayerPreferencesScreen
import com.planes.android.video.VideoScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PlanesNavigation(modifier: Modifier, navController: NavHostController, context: Context) {

    NavHost(
        navController = navController,
        startDestination = PlanesScreens.Info.name) {
        composable(PlanesScreens.SinglePlayerGame.name) {
           SinglePlayerGameScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.SinglePlayerGameStatistics.name) {
            SinglePlayerGameStatisticsScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.SinglePlayerPreferences.name) {
            SinglePlayerPreferencesScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.MultiplayerGame.name) {
            MultiplayerGameScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.MultiplayerGameStatistics.name) {
            MultiplayerGameStatisticsScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.MultiplayerPreferences.name) {
            MultiplayerPreferencesScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.Info.name) {
            AboutScreen(modifier = modifier, navController = navController,
                aboutEntryList = AboutEntryRepository.create("0.1", context = context))
        }
        composable(PlanesScreens.Tutorials.name) {
            VideoScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.Login.name) {
            LoginScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.Logout.name) {
            LogoutScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.Register.name) {
            RegisterScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.DeleteUser.name) {
            DeleteUserScreen(modifier = modifier, navController = navController)
        }
        composable(PlanesScreens.Chat.name) {
            ChatScreen(modifier = modifier, navController = navController)
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