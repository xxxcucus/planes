package com.planes.android.singleplayergame

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.planes.singleplayerengine.PlanesRoundInterface
import java.util.Date
import kotlin.math.abs

@Composable
fun BoardEditingScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                       topBarHeight: MutableState<Int>,
                       navController: NavController,
                       planeRound: PlanesRoundInterface,
                       playerGridViewModel: PlaneGridViewModel) {

    currentScreenState.value = PlanesScreens.SinglePlayerGame.name

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

            Column(modifier = Modifier.height(screenHeightDp.dp - boardSizeDp.dp),
                verticalArrangement = Arrangement.Center) {
                Row(
                   horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(buttonHeightDp.dp).fillMaxWidth()
                ) {
                    OneLineGameButton(
                        textLine = stringResource(R.string.rotate_button), playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = true
                    ) { viewModel ->
                        viewModel.rotatePlane(playerGridViewModel.getSelectedPlane())
                    }
                    OneLineGameButton(
                        textLine = stringResource(R.string.done_button), playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = !playerGridViewModel.isPlaneOutsideGrid() && !playerGridViewModel.doPlanesOverlap()
                    ) {
                        playerGridViewModel.updatePlanesToPlaneRound()
                        playerGridViewModel.doneEditing()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.SinglePlayerGame.name)
                    }
                }

                Row(
                   horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(buttonHeightDp.dp).fillMaxWidth()
                ) {
                    OneLineGameButton(
                        textLine = stringResource(R.string.cancel), playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = true
                    ) {
                        planeRound.cancelRound()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.SinglePlayerGameNotStarted.name)
                    }
                    TwoLineGameButton(
                        textLine1 = stringResource(R.string.reset_board1),
                        textLine2 = stringResource(R.string.reset_board2),
                        playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = true
                    ) { viewModel ->
                        viewModel.initGrid()
                    }
                }
            }
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

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight().width(buttonWidthDp.dp)
                        .padding(top = topBarHeight.value.dp)
                ) {
                    OneLineGameButton(
                        textLine = stringResource(R.string.rotate_button), playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = true
                    ) { viewModel ->
                        viewModel.rotatePlane(playerGridViewModel.getSelectedPlane())
                    }

                    OneLineGameButton(
                        textLine = stringResource(R.string.cancel), playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = true
                    ) {
                        planeRound.cancelRound()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.SinglePlayerGameNotStarted.name)
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight().width(buttonWidthDp.dp)
                ) {
                    Spacer(modifier = Modifier.height(topBarHeight.value.dp))
                    OneLineGameButton(
                        textLine = stringResource(R.string.done_button), playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = !playerGridViewModel.isPlaneOutsideGrid() && !playerGridViewModel.doPlanesOverlap()
                    ) {
                        playerGridViewModel.updatePlanesToPlaneRound()
                        playerGridViewModel.doneEditing()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.SinglePlayerGame.name)
                    }

                    TwoLineGameButton(
                        textLine1 = stringResource(R.string.reset_board1),
                        textLine2 = stringResource(R.string.reset_board2),
                        playerGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                        enabled = true
                    ) { viewModel ->
                        viewModel.initGrid()
                    }
                }
            }
        }
    }
}

fun treatSwipeVertical(swipeThresh: Float, consecSwipeThresh: Int,
               swipeLengthX: Float, swipeLengthY: Float,
               squareSizePx: Float,
               curTime: Date, dragAmount: Offset,
               planesGridViewModel: PlaneGridViewModel) : Triple<Float, Float, Date> {
    val t = Date()
    val diff = -curTime.time + t.time
    if (diff < consecSwipeThresh) {
        Log.i("Tag", "Dragged $diff ms")
        return Triple(swipeLengthX + dragAmount.x, swipeLengthY + dragAmount.y, curTime)
    }

    if (abs(swipeLengthX) > abs(swipeLengthY)) {

        val steps = (abs(swipeLengthX) / squareSizePx).toInt()
        Log.i("Tag", "Dragged ${abs(swipeLengthX)} $steps $squareSizePx")
        //val steps = 0
        if (swipeLengthX > swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneRight(planesGridViewModel.getSelectedPlane())
            //down
        } else if (swipeLengthX < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneLeft(planesGridViewModel.getSelectedPlane())
            //up
        }
    } else {

        val steps = (abs(swipeLengthY) / squareSizePx).toInt()
        Log.i("Tag", "Dragged ${abs(swipeLengthY)} $steps $squareSizePx")
        //val steps = 0
        if (swipeLengthY > swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneDownwards(planesGridViewModel.getSelectedPlane())
            //right
        } else if (swipeLengthY < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneUpwards(planesGridViewModel.getSelectedPlane())
            //left
        }
    }

    return Triple(0.0f, 0.0f, t)
}


fun treatSwipeHorizontal(swipeThresh: Float, consecSwipeThresh: Int,
                       swipeLengthX: Float, swipeLengthY: Float,
                       squareSizePx: Float,
                       curTime: Date, dragAmount: Offset,
                       planesGridViewModel: PlaneGridViewModel) : Triple<Float, Float, Date> {
    val t = Date()
    val diff = -curTime.time + t.time
    if (diff < consecSwipeThresh) {
        Log.i("Tag", "Dragged $diff ms")
        return Triple(swipeLengthX + dragAmount.x, swipeLengthY + dragAmount.y, curTime)
    }

    if (abs(swipeLengthX) > abs(swipeLengthY)) {

        val steps = (abs(swipeLengthX) / squareSizePx).toInt()
        Log.i("Tag", "Dragged ${abs(swipeLengthX)} $steps $squareSizePx")
        //val steps = 0
        if (swipeLengthX > swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneRight(planesGridViewModel.getSelectedPlane())
        } else if (swipeLengthX < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneLeft(planesGridViewModel.getSelectedPlane())
        }
    } else {

        val steps = (abs(swipeLengthY) / squareSizePx).toInt()
        Log.i("Tag", "Dragged ${abs(swipeLengthY)} $steps $squareSizePx")
        //val steps = 0
        if (swipeLengthY > swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneDownwards(planesGridViewModel.getSelectedPlane())
        } else if (swipeLengthY < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneUpwards(planesGridViewModel.getSelectedPlane())
        }
    }

    return Triple(0.0f, 0.0f, t)
}