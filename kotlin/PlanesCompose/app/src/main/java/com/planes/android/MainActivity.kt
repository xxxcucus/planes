package com.planes.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.planes.android.navigation.DrawerMenuItemGeneric
import com.planes.android.navigation.PlanesNavigation
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.preferences.PreferencesViewModel
import com.planes.android.ui.theme.PlanesComposeTheme
import com.planes.android.widgets.HelpPopupBox
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.GameStages
import com.planes.singleplayerengine.SinglePlayerRoundInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var planeRound: SinglePlayerRoundInterface
    @Inject lateinit var planeRoundMultiplayer: MultiPlayerRoundInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlanesComposeTheme {
                val navController = rememberNavController()
                Screen(modifier = Modifier,
                    navController = navController,
                    planeRound = planeRound,
                    planeRoundMultiplayer = planeRoundMultiplayer)
            }
        }
    }

    //TODO onResume, onPause

}

@Composable
fun Screen(modifier: Modifier,
           navController: NavHostController,
           planeRound: SinglePlayerRoundInterface,
           planeRoundMultiplayer: MultiPlayerRoundInterface) {

    val optionsViewModel: PreferencesViewModel = hiltViewModel()
    planeRound.setComputerSkill(optionsViewModel.getComputerSkill())
    planeRound.setShowPlaneAfterKill(optionsViewModel.getShowPlaneAfterKill())

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val currentTitleState = remember {
        mutableStateOf("About")
    }

    val currentScreenState = remember {
        mutableStateOf(PlanesScreens.Info.name)
    }

    val showPopupState = remember {
        mutableStateOf(false)
    }

    val newMessagesState = remember {
        mutableStateOf(false)
    }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    navController = navController,
                    drawerScope = scope,
                    drawerState = drawerState,
                    planeRound = planeRound,
                    planeRoundMultiplayer = planeRoundMultiplayer
                )
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    modifier = Modifier.padding(0.dp).fillMaxWidth(),
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
                    title = currentTitleState.value,
                    currentScreen = currentScreenState.value,
                    newMessages = newMessagesState.value,
                    showPopupState = showPopupState
                )
            }
        ) { padding ->

            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                ScreenContent(
                    modifier = Modifier.align(Alignment.Center),
                    currentTitleState = currentTitleState,
                    currentScreenState = currentScreenState,
                    showPopupState = showPopupState,
                    newMessagesState = newMessagesState,
                    navController = navController,
                    planeRound,
                    planeRoundMultiplayer
                )


                HelpPopupBox(
                    modifier = Modifier.align(Alignment.Center),
                    currentScreenState = currentScreenState,
                    showPopupState = showPopupState,
                    screenWidth = screenWidthDp.toFloat(),
                    screenHeight = screenHeightDp.toFloat()
                )

            }
        }
    }
}



@Composable
fun ScreenContent(modifier: Modifier, currentTitleState: MutableState<String>,
                  currentScreenState: MutableState<String>,
                  showPopupState: MutableState<Boolean>,
                  newMessagesState : MutableState<Boolean>,
                  navController: NavHostController,
                  planeRound: SinglePlayerRoundInterface,
                  planeRoundMultiplayer: MultiPlayerRoundInterface
                  ) {

    PlanesNavigation(modifier = modifier,
        currentTitleState, currentScreenState,
        showPopupState,newMessagesState,
        navController,
        context = LocalContext.current,
        planeRound,
        planeRoundMultiplayer)
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier,
                  navController: NavController,
                  drawerScope: CoroutineScope,
                  drawerState: DrawerState,
                  planeRound: SinglePlayerRoundInterface,
                  planeRoundMultiplayer: MultiPlayerRoundInterface
                  ) {
    //TODO: page names from resources

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

        //TODO: Resources strings
        Text(
            text = "Connectivity",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        DrawerMenuItemGeneric("Login / Logout", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.Login.name)
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

        DrawerMenuItemGeneric("Delete User", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.DeleteUser.name)
        })

        Text(
            text = "Various Games",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

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

        DrawerMenuItemGeneric("Create Multiplayer Game", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.CreateMultiplayerGame.name)
        })

        DrawerMenuItemGeneric("Multiplayer Game", {
            drawerScope.launch {
                drawerState.close()
            }
            if (planeRoundMultiplayer.getGameStage() == GameStages.BoardEditing)
                navController.navigate(route = PlanesScreens.MultiplayerBoardEditing.name)
            else if (planeRoundMultiplayer.getGameStage() == GameStages.Game)
                navController.navigate(route = PlanesScreens.MultiplayerGame.name)
            else
                navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
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
            style = MaterialTheme.typography.titleLarge
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier,
           onOpenDrawer: () -> Unit = {},
           title: String,
           currentScreen: String,
           newMessages: Boolean,
           showPopupState: MutableState<Boolean>
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
            Text(text = title)
        },
        actions = {
            if (newMessages) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Navigation Icon",
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                        .size(28.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = "Navigation Icon",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp).size(28.dp)
            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Navigation Icon",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    .size(28.dp).clickable {
                    if (currentScreen in listOf(PlanesScreens.SinglePlayerBoardEditing.name,
                            PlanesScreens.MultiplayerBoardEditing.name, PlanesScreens.SinglePlayerGame.name,
                        PlanesScreens.MultiplayerGame.name, PlanesScreens.SinglePlayerGameNotStarted.name,
                        PlanesScreens.MultiplayerGameNotStarted.name, PlanesScreens.Tutorials.name,
                        PlanesScreens.Chat.name, PlanesScreens.Conversation.name))
                    showPopupState.value = true
            })
        },
        windowInsets = WindowInsets.statusBars
    )
}

