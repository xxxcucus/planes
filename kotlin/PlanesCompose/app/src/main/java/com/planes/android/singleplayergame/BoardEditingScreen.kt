package com.planes.android.singleplayergame

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import java.util.Date
import kotlin.math.abs

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BoardEditingScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                       topBarHeight: MutableState<Int>,
                       navController: NavController,
                       planesGridViewModel: PlaneGridViewModel) {

    currentScreenState.value = PlanesScreens.SinglePlayerGame.name

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    var squareSizeDp = screenWidthDp / planesGridViewModel.getColNo()

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //100 is topBar height
        squareSizeDp = (screenHeightDp - topBarHeight.value) / planesGridViewModel.getRowNo()
    }

    var buttonHeightDp = (screenHeightDp - planesGridViewModel.getColNo() * squareSizeDp - 100) / 4

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        buttonHeightDp = screenHeightDp / 4
    }

    //val buttonHeightDp = 100
    var buttonWidthDp = screenWidthDp / 3

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        buttonWidthDp = (screenWidthDp - planesGridViewModel.getColNo() * squareSizeDp - 100) / 4
    }

    //TODO: to optimize for horizontal layout
    val squareSizePx = with(LocalDensity.current) { squareSizeDp.dp.toPx() }
    val swipeThresh = 20.0f
    val consecSwipeThresh = 100
    var swipeLengthX = 0.0f
    var swipeLengthY = 0.0f
    var curTime = Date()

    //Log.d("Planes", "planes no ${planesGridViewModel.getPlaneNo()}")

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column() {
            LazyVerticalGrid(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Top,
                columns = GridCells.Fixed(planesGridViewModel.getColNo()),
                userScrollEnabled = false,
                modifier = modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            val tripleVal = treatSwipeVertical(swipeThresh, consecSwipeThresh, swipeLengthX,
                                swipeLengthY, squareSizePx, curTime, dragAmount, planesGridViewModel)
                            swipeLengthX = tripleVal.first
                            swipeLengthY = tripleVal.second
                            curTime = tripleVal.third
                        }
                    )
                }
            ) {
                items(planesGridViewModel.getRowNo() * planesGridViewModel.getColNo()) { index ->
                    BoardSquare(index, squareSizeDp, squareSizePx, planesGridViewModel, true) {
                        val row = index / planesGridViewModel.getColNo()
                        val col = index % planesGridViewModel.getColNo()

                        planesGridViewModel.setSelectedPlane(col, row)
                    }
                }
            }

            Column(modifier = Modifier.fillMaxHeight(0.8f),
                verticalArrangement = Arrangement.Center) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GameButton(
                        title = stringResource(R.string.rotate_button), planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) { viewModel ->
                        viewModel.rotatePlane(planesGridViewModel.getSelectedPlane())
                    }
                    GameButton(
                        title = stringResource(R.string.done_button), planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) {
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GameButton(
                        title = stringResource(R.string.cancel), planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) {
                    }
                    GameButton(
                        title = stringResource(R.string.reset_board1) + " " + stringResource(R.string.reset_board2),
                        planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) { viewModel ->
                        viewModel.initGrid()
                    }
                }
            }
        }
    } else {
        Row() {
            LazyHorizontalGrid(
                horizontalArrangement = Arrangement.Start,
                verticalArrangement = Arrangement.Center,
                rows = GridCells.Fixed(planesGridViewModel.getRowNo()),
                userScrollEnabled = false,
                modifier = modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            val tripleVal = treatSwipeHorizontal(
                                swipeThresh, consecSwipeThresh, swipeLengthX,
                                swipeLengthY, squareSizePx, curTime, dragAmount, planesGridViewModel
                            )
                            swipeLengthX = tripleVal.first
                            swipeLengthY = tripleVal.second
                            curTime = tripleVal.third
                        })
                    }
            ) {
                items(planesGridViewModel.getRowNo() * planesGridViewModel.getColNo()) { index ->
                    BoardSquare(index, squareSizeDp, squareSizePx, planesGridViewModel, false) {
                        val row = index / planesGridViewModel.getColNo()
                        val col = index % planesGridViewModel.getColNo()

                        planesGridViewModel.setSelectedPlane(row, col)
                    }
                }
            }

            //Text(text = "This is a test")
            Row(modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.height(topBarHeight.value.dp))
                    GameButton(
                        title = stringResource(R.string.rotate_button), planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) { viewModel ->
                        viewModel.rotatePlane(planesGridViewModel.getSelectedPlane())
                    }

                    GameButton(
                        title = stringResource(R.string.cancel), planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) {
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.height(topBarHeight.value.dp))
                    GameButton(
                        title = stringResource(R.string.done_button), planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) {
                    }

                    GameButton(
                        title = stringResource(R.string.reset_board1) + " " + stringResource(R.string.reset_board2),
                        planesGridViewModel,
                        modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp)
                    ) { viewModel ->
                        viewModel.initGrid()
                    }
                }
            }
        }
    }
}

@Composable
fun BoardSquare(index: Int, squareSizeDp: Int, squareSizePx: Float,
                planesGridViewModel: PlaneGridViewModel, isHoriz: Boolean,
                onClick: (Int) -> Unit) {
    val row = index / planesGridViewModel.getColNo()
    val col = index % planesGridViewModel.getColNo()

    var pointOnPlane = planesGridViewModel.isPointOnPlane(col, row)
    if (isHoriz)
        pointOnPlane = planesGridViewModel.isPointOnPlane(row, col)
    if (!pointOnPlane.first)
        GridSquare(squareSizeDp, squareSizePx, Color.Blue)
    else {
        val annotation = planesGridViewModel.getAnnotation(pointOnPlane.second)
        val planesIdx = planesGridViewModel.decodeAnnotation(annotation)
        if (planesIdx.size == 1) {
            GridSquare(
                isComputer = planesGridViewModel.isComputer(),
                selectedPlane = planesGridViewModel.getSelectedPlane(),
                annotation = if (planesIdx[0] < 0) -2 else planesIdx[0] + 1,
                widthDp = squareSizeDp,
                widthPx = squareSizePx,
                backgroundColor = Color.Blue,
                index = index,
                onClick = onClick
            )
            //Log.d("Planes", "plane ${planesIdx[0]}")
        } else {
            GridSquare(
                isComputer = planesGridViewModel.isComputer(),
                selectedPlane = planesGridViewModel.getSelectedPlane(),
                annotation = -1,
                widthDp = squareSizeDp,
                widthPx = squareSizePx,
                backgroundColor = Color.Blue,
                index = index,
                onClick = onClick
            )
        }
    }
}

@Composable
fun GameButton(title: String, planesGridViewModel: PlaneGridViewModel,
               modifier: Modifier,
               onClick: (PlaneGridViewModel) -> Unit) {
    Card(
        modifier = modifier.padding(1.dp).clickable {
            onClick.invoke(planesGridViewModel)
        },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = title
            )
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
                planesGridViewModel.movePlaneDownwards(planesGridViewModel.getSelectedPlane())
            //down
        } else if (swipeLengthX < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneUpwards(planesGridViewModel.getSelectedPlane())
            //up
        }
    } else {

        val steps = (abs(swipeLengthY) / squareSizePx).toInt()
        Log.i("Tag", "Dragged ${abs(swipeLengthY)} $steps $squareSizePx")
        //val steps = 0
        if (swipeLengthY > swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneRight(planesGridViewModel.getSelectedPlane())
            //right
        } else if (swipeLengthY < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneLeft(planesGridViewModel.getSelectedPlane())
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
                planesGridViewModel.movePlaneDownwards(planesGridViewModel.getSelectedPlane())
        } else if (swipeLengthX < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneUpwards(planesGridViewModel.getSelectedPlane())
        }
    } else {

        val steps = (abs(swipeLengthY) / squareSizePx).toInt()
        Log.i("Tag", "Dragged ${abs(swipeLengthY)} $steps $squareSizePx")
        //val steps = 0
        if (swipeLengthY > swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneRight(planesGridViewModel.getSelectedPlane())
        } else if (swipeLengthY < -swipeThresh) {
            for (i in 0..<steps)
                planesGridViewModel.movePlaneLeft(planesGridViewModel.getSelectedPlane())
        }
    }

    return Triple(0.0f, 0.0f, t)
}