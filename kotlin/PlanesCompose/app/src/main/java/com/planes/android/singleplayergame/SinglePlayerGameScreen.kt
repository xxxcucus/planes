package com.planes.android.singleplayergame

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.planes.android.navigation.PlanesScreens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SinglePlayerGameScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                           navController: NavController,
                           planesGridViewModel: PlaneGridViewModel = viewModel()) {

    currentScreenState.value = PlanesScreens.SinglePlayerGame.name

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    var squareSizeDp = screenWidthDp / planesGridViewModel.getColNo()

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        squareSizeDp = screenHeightDp / planesGridViewModel.getRowNo()
    }

    //TODO: to optimize for horizontal layout
    val squareSizePx = with(LocalDensity.current) { squareSizeDp.dp.toPx() }

    Log.d("Planes", "planes no ${planesGridViewModel.getPlaneNo()}")

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Top,
            columns = GridCells.Fixed(planesGridViewModel.getColNo()),
            modifier = modifier.fillMaxHeight()
        ) {
            items(planesGridViewModel.getRowNo() * planesGridViewModel.getColNo()) { index ->
                DrawBoardSquare(index, squareSizeDp, squareSizePx, planesGridViewModel)
            }
        }
    } else {
        LazyHorizontalGrid(
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.Center,
            rows = GridCells.Fixed(planesGridViewModel.getRowNo()),
            modifier = modifier.fillMaxWidth()
        ) {
            items(planesGridViewModel.getRowNo() * planesGridViewModel.getColNo()) { index ->
                DrawBoardSquare(index, squareSizeDp, squareSizePx, planesGridViewModel)
            }
        }
    }

    //TODO: define layout for tablet



}

@Composable
fun DrawBoardSquare(index: Int, squareSizeDp: Int, squareSizePx: Float, planesGridViewModel: PlaneGridViewModel) {
    val row = index / planesGridViewModel.getColNo()
    val col = index % planesGridViewModel.getColNo()

    val pointOnPlane = planesGridViewModel.isPointOnPlane(col, row)
    if (!pointOnPlane.first)
        GridSquare(squareSizeDp, squareSizePx, Color.Blue)
    else {
        val annotation = planesGridViewModel.getAnnotation(pointOnPlane.second)
        val planesIdx = planesGridViewModel.decodeAnnotation(annotation)
        if (planesIdx.size == 1) {
            GridSquare(
                planesGridViewModel.isComputer(),
                if (planesIdx[0] < 0) -2 else planesIdx[0] + 1,
                squareSizeDp,
                squareSizePx,
                Color.Blue
            )
            Log.d("Planes", "plane ${planesIdx[0]}")
        } else {
            GridSquare(
                planesGridViewModel.isComputer(),
                -1,
                squareSizeDp,
                squareSizePx,
                Color.Blue
            )
        }
    }
}

