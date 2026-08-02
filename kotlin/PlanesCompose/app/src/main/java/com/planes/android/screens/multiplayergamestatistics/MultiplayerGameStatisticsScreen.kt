package com.planes.android.screens.multiplayergamestatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun MultiplayerGameStatisticsScreen(modifier: Modifier,
                                    currentTitleState: MutableState<String>,
                                    currentScreenState: MutableState<String>,
                                    showPopupState: MutableState<Boolean>,
                                    navController: NavController) {

    currentTitleState.value = stringResource(R.string.game_stats)
    currentScreenState.value = PlanesScreens.MultiplayerGameStatistics.name
    showPopupState.value = false

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Multiplayer Game Statistics Screen")
    }
}