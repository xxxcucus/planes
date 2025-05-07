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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.planes.android.navigation.DrawerMenuItemGeneric
import com.planes.android.navigation.PlanesNavigation
import com.planes.android.navigation.PlanesScreens
import com.planes.android.ui.theme.PlanesComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanesComposeTheme {
                val navController = rememberNavController()
                Screen(modifier = Modifier, navController = navController)
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier, navController: NavHostController) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navController = navController, drawerScope = scope, drawerState = drawerState)
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    modifier = Modifier.padding(30.dp)
                        .height(100.dp).fillMaxWidth(),
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed)
                                    open()
                                else
                                    close()
                            }
                        }
                    }
                )
            }
        ) { padding ->
            ScreenContent(modifier = Modifier.padding(padding),
                navController = navController)
        }
    }
}

@Composable
fun ScreenContent(modifier: Modifier,
                  navController: NavHostController
) {
    PlanesNavigation(navController)
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier,
                  navController: NavController,
                  drawerScope: CoroutineScope,
                  drawerState: DrawerState,
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

        Text(
            text = "Single Player Game",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        DrawerMenuItemGeneric("Game", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.SinglePlayerGame.name)
        })

        DrawerMenuItemGeneric("Preferences", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.SinglePlayerPreferences.name)
        })

        DrawerMenuItemGeneric("Game Statistics", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.SinglePlayerGameStatistics.name)
        })


        Text(
            text = "Multiplayer Game",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        DrawerMenuItemGeneric("Game", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.MultiplayerGame.name)
        })

        DrawerMenuItemGeneric("Preferences", {
            drawerScope.launch {
                drawerState.close()
            }
            navController.navigate(route = PlanesScreens.MultiplayerPreferences.name)
        })

        DrawerMenuItemGeneric("Game Statistics", {
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
    onOpenDrawer: () -> Unit = {}
) {
    TopAppBar(
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
            Text(text = "Screen Name")
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

@Preview
@Composable
fun TestTopBar() {
    TopBar()
}