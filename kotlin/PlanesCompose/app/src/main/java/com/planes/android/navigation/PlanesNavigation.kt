package com.planes.android.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.planes.android.about.AboutScreen
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
fun PlanesNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = PlanesScreens.Info.name) {
        composable(PlanesScreens.SinglePlayerGame.name) {
           SinglePlayerGameScreen(navController = navController)
        }
        composable(PlanesScreens.SinglePlayerGameStatistics.name) {
            SinglePlayerGameStatisticsScreen(navController = navController)
        }
        composable(PlanesScreens.SinglePlayerPreferences.name) {
            SinglePlayerPreferencesScreen(navController = navController)
        }
        composable(PlanesScreens.MultiplayerGame.name) {
            MultiplayerGameScreen(navController = navController)
        }
        composable(PlanesScreens.MultiplayerGameStatistics.name) {
            MultiplayerGameStatisticsScreen(navController = navController)
        }
        composable(PlanesScreens.MultiplayerPreferences.name) {
            MultiplayerPreferencesScreen(navController = navController)
        }
        composable(PlanesScreens.Info.name) {
            AboutScreen(navController = navController)
        }
        composable(PlanesScreens.Tutorials.name) {
            VideoScreen(navController = navController)
        }
        composable(PlanesScreens.Login.name) {
            LoginScreen(navController = navController)
        }
        composable(PlanesScreens.Logout.name) {
            LogoutScreen(navController = navController)
        }
        composable(PlanesScreens.Register.name) {
            RegisterScreen(navController = navController)
        }
        composable(PlanesScreens.DeleteUser.name) {
            DeleteUserScreen(navController = navController)
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