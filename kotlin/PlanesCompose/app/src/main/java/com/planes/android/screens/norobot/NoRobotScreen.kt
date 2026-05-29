package com.planes.android.screens.norobot

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun NoRobotScreen(modifier: Modifier, currentTitleState: MutableState<String>,
                  currentScreenState: MutableState<String>,
                  showPopupState: MutableState<Boolean>,
                  navController: NavController, noRobotViewModel: NoRobotViewModel
) {
    currentTitleState.value = stringResource(R.string.register)
    currentScreenState.value = PlanesScreens.NoRobot.name
    showPopupState.value = false

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    var squareSizeDp = screenWidthDp / 2

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //squareSizeDp = (screenHeightDp - topBarHeight.value) / playerGridViewModel.getRowNo()
        squareSizeDp = screenHeightDp / 2
    }

    val submitClickedState = rememberSaveable {
        mutableStateOf(false)
    }

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = LocalContext.current.getString(
                R.string.norobot_question,
                noRobotViewModel.getQuestion()
            )
        )
        Button(
            modifier = Modifier,
            onClick = {
                submitClickedState.value = true
                noRobotViewModel.noRobotRequest()
            }) {
            Text(text = stringResource(R.string.norobot_allmarked))
        }

        if (submitClickedState.value && noRobotViewModel.getLoading()) {
            Text(text = stringResource(R.string.loader_text))
        } else if (submitClickedState.value) {
            val error = noRobotViewModel.getError()

            if (error == null) {

                if (!noRobotViewModel.responseAvailable()) {
                    Toast.makeText(
                        LocalContext.current,
                        "No data available", //TODO
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.norobot_success),
                        Toast.LENGTH_LONG
                    ).show()

                    //TODO: delete screen from history
                    navController.navigate(PlanesScreens.Login.name)
                }
            } else {
                Toast.makeText(
                    LocalContext.current,
                    noRobotViewModel.getError(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LazyVerticalGrid(
                modifier = Modifier,
                columns = GridCells.Adaptive(minSize = squareSizeDp.dp)
            ) {
                itemsIndexed(items = noRobotViewModel.getImages()) { index, item ->
                    NoRobotEntryRow(noRobotViewModel, index, squareSizeDp,false)
                }
            }
        } else {
            LazyHorizontalGrid(
                modifier = Modifier,
                rows = GridCells.Adaptive(minSize = squareSizeDp.dp)
            ) {
                itemsIndexed(items = noRobotViewModel.getImages()) { index, item ->
                    NoRobotEntryRow(noRobotViewModel, index, squareSizeDp, true)
                }
            }
        }
    }
}