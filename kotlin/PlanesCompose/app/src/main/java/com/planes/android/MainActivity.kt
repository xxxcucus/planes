package com.planes.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.planes.android.navigation.DrawerMenuItemGeneric
import com.planes.android.navigation.PlanesNavigation
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.preferences.PreferencesViewModel
import com.planes.android.screens.register.RegisterViewModel
import com.planes.android.screens.singleplayergame.ComputerGridViewModel
import com.planes.android.screens.singleplayergame.GameStatsViewModel
import com.planes.android.screens.singleplayergame.PlayerGridViewModel
import com.planes.android.ui.theme.PlanesComposeTheme
import com.planes.singleplayerengine.GameStages
import com.planes.singleplayerengine.PlaneRound
import com.planes.singleplayerengine.PlanesRoundInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var planeRound: PlanesRoundInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlanesComposeTheme {
                val navController = rememberNavController()
                Screen(modifier = Modifier,
                    navController = navController,
                    planeRound = planeRound)
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier,
           navController: NavHostController,
           planeRound: PlanesRoundInterface) {

    val playerGridViewModel: PlayerGridViewModel = hiltViewModel()
    val computerGridViewModel: ComputerGridViewModel = hiltViewModel()
    val optionsViewModel: PreferencesViewModel = hiltViewModel()
    val gameStatsViewModel: GameStatsViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val registerViewModel: RegisterViewModel = hiltViewModel()

    planeRound.setComputerSkill(optionsViewModel.getComputerSkill())
    planeRound.setShowPlaneAfterKill(optionsViewModel.getShowPlaneAfterKill())

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val currentScreenState = remember {
        mutableStateOf("About")
    }

    val topBarHeight = remember {
        mutableStateOf(70)
    }

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    navController = navController,
                    drawerScope = scope,
                    drawerState = drawerState,
                    planeRound = planeRound
                )
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    modifier = Modifier.padding(0.dp)
                        .height(topBarHeight.value.dp).fillMaxWidth(),
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed)
                                    open()
                                else
                                    close()
                            }
                        }
                    },
                    currentScreenName = currentScreenState.value
                )
            }
        ) { padding ->
            ScreenContent(modifier = Modifier.padding(padding),
                currentScreenState = currentScreenState,
                topBarHeight = topBarHeight,
                navController = navController,
                planeRound,
                optionsViewModel,
                playerGridViewModel,
                computerGridViewModel,
                gameStatsViewModel,
                loginViewModel,
                registerViewModel)
        }
    }
}

@Composable
fun ScreenContent(modifier: Modifier, currentScreenState: MutableState<String>,
                  topBarHeight: MutableState<Int>,
                  navController: NavHostController,
                  planeRound: PlanesRoundInterface,
                  optionsViewModel: PreferencesViewModel,
                  playerGridViewModel: PlayerGridViewModel,
                  computerGridViewModel: ComputerGridViewModel,
                  gameStatsViewModel: GameStatsViewModel,
                  loginViewModel: LoginViewModel,
                  registerViewModel: RegisterViewModel
) {
    PlanesNavigation(modifier = modifier,
        currentScreenState, topBarHeight, navController,
        context = LocalContext.current,
        planeRound,
        optionsViewModel,
        playerGridViewModel,
        computerGridViewModel,
        gameStatsViewModel,
        loginViewModel,
        registerViewModel)
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier,
                  navController: NavController,
                  drawerScope: CoroutineScope,
                  drawerState: DrawerState,
                  planeRound: PlanesRoundInterface
                  ) {

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Planes",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalDivider()

        DrawerMenuItemGeneric("Login", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Login.name)
        })

        DrawerMenuItemGeneric("Logout", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Logout.name)
        })

        DrawerMenuItemGeneric("Register", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Register.name)
        })

        DrawerMenuItemGeneric("Chat", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Chat.name)
        })

        DrawerMenuItemGeneric("Single Player Game", {
            drawerScope.launch {
                drawerState.close()
            }

            if (planeRound.getGameStage() == GameStages.BoardEditing)
                navController.navigate(route = PlanesScreens.SinglePlayerBoardEditing.name)
            else if (planeRound.getGameStage() == GameStages.Game)
                navController.navigate(route = PlanesScreens.SinglePlayerGame.name)
            else
                navController.navigate(route = PlanesScreens.SinglePlayerGameNotStarted.name)
        })



        DrawerMenuItemGeneric("Single Player Game Statistics", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.SinglePlayerGameStatistics.name)
        })


        DrawerMenuItemGeneric("Multiplayer Game", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.MultiplayerGame.name)
        })

        DrawerMenuItemGeneric("Multiplayer Game Statistics", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.MultiplayerGameStatistics.name)
        })

        Text(
            text = "Info",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        DrawerMenuItemGeneric("About", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Info.name)
        })

        DrawerMenuItemGeneric("Tutorials", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Tutorials.name)
        })

        DrawerMenuItemGeneric("Preferences", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Preferences.name)
        })

        DrawerMenuItemGeneric("Delete User", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.DeleteUser.name)
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier,
    onOpenDrawer: () -> Unit = {},
    currentScreenName: String
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Navigation Icon",
                modifier = Modifier.clickable {
                    onOpenDrawer()
                }. padding(start = 16.dp, end = 8.dp)
                    .size(28.dp))
        },
        title = {
            Text(text = currentScreenName)
        },
        actions = {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = "Navigation Icon",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp).size(28.dp)
            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Navigation Icon",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    .size(28.dp))
        }
    )
}

