package com.planes.android.screens.multiplayergame

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.singleplayergame.BoardEditingControlButtonsHorizontalLayout
import com.planes.android.screens.singleplayergame.BoardEditingControlButtonsVerticalLayout
import com.planes.android.screens.singleplayergame.BoardSquareBoardEditing
import com.planes.android.screens.singleplayergame.GameBoardSinglePlayer
import com.planes.android.screens.singleplayergame.OneLineGameButton
import com.planes.android.screens.singleplayergame.PlaneGridViewModel
import com.planes.android.screens.singleplayergame.TwoLineGameButton
import com.planes.android.screens.singleplayergame.treatSwipeHorizontal
import com.planes.android.screens.singleplayergame.treatSwipeVertical
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.SinglePlayerRoundInterface
import java.util.Date
import kotlin.math.abs

@Composable
fun BoardEditingScreenMultiPlayer(modifier: Modifier, currentScreenState: MutableState<String>,
                                  topBarHeight: MutableState<Int>,
                                  navController: NavController,
                                  planeRound: MultiPlayerRoundInterface,
                                  playerGridViewModel: PlayerGridViewModelMultiPlayer
) {

    currentScreenState.value = PlanesScreens.MultiplayerGame.name

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    var squareSizeDp = screenWidthDp / playerGridViewModel.getColNo()

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //squareSizeDp = (screenHeightDp - topBarHeight.value) / playerGridViewModel.getRowNo()
        squareSizeDp = screenHeightDp / playerGridViewModel.getRowNo()
    }

    var boardSizeDp = squareSizeDp * playerGridViewModel.getRowNo()

    var buttonHeightDp = (screenHeightDp - boardSizeDp) / 4

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        buttonHeightDp = screenHeightDp / 4
    }

    var buttonWidthDp = screenWidthDp / 3

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        buttonWidthDp = (screenWidthDp - boardSizeDp) / 3
    }

    val squareSizePx = with(LocalDensity.current) { squareSizeDp.dp.toPx() }
    val swipeThresh = 20.0f
    val consecSwipeThresh = 100
    var swipeLengthX = 0.0f
    var swipeLengthY = 0.0f
    var curTime = Date()

    //Log.d("Planes", "planes no ${planesGridViewModel.getPlaneNo()}")

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column() {

            GameBoardSinglePlayer(playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { _, dragAmount ->
                                val tripleVal = treatSwipeVertical(swipeThresh, consecSwipeThresh, swipeLengthX,
                                    swipeLengthY, squareSizePx, curTime, dragAmount, playerGridViewModel)
                                swipeLengthX = tripleVal.first
                                swipeLengthY = tripleVal.second
                                curTime = tripleVal.third
                            }
                        )
                    }) {
                for (index in 0..99)
                    BoardSquareBoardEditing(index, squareSizeDp, squareSizePx, playerGridViewModel) {
                        val row = index / playerGridViewModel.getColNo()
                        val col = index % playerGridViewModel.getColNo()

                        playerGridViewModel.setSelectedPlane(row, col)
                    }
            }

            BoardEditingControlButtonsVerticalLayout(screenHeightDp, boardSizeDp, buttonHeightDp,
                buttonWidthDp, navController,
                playerGridViewModel,
                planeRound)
        }
    } else {  //landscape
        Row() {

            GameBoardSinglePlayer(playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { _, dragAmount ->
                                val tripleVal = treatSwipeHorizontal(
                                    swipeThresh, consecSwipeThresh, swipeLengthX,
                                    swipeLengthY, squareSizePx, curTime, dragAmount, playerGridViewModel
                                )
                                swipeLengthX = tripleVal.first
                                swipeLengthY = tripleVal.second
                                curTime = tripleVal.third
                            })
                    }) {
                for (index in 0..99)
                    BoardSquareBoardEditing(index, squareSizeDp, squareSizePx, playerGridViewModel) {
                        val row = index / playerGridViewModel.getColNo()
                        val col = index % playerGridViewModel.getColNo()

                        playerGridViewModel.setSelectedPlane(row, col)
                    }
            }

            BoardEditingControlButtonsHorizontalLayout(screenHeightDp, boardSizeDp, buttonHeightDp,
                buttonWidthDp, topBarHeight.value, navController,
                playerGridViewModel,
                planeRound)
        }
    }
}

