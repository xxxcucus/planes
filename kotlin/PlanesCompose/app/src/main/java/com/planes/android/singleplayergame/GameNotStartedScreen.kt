package com.planes.android.singleplayergame

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.navigation.PlanesScreens

@Composable
fun GameNotStartedScreen(modifier: Modifier, currentScreenState: MutableState<String>,
               topBarHeight: MutableState<Int>,
               navController: NavController,
               playerGridViewModel: PlaneGridViewModel,
               computerGridViewModel: PlaneGridViewModel
) {

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
    val squareSizePx = with(LocalDensity.current) { squareSizeDp.dp.toPx() }

    //Log.d("Planes", "planes no ${planesGridViewModel.getPlaneNo()}")

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column() {
            GameBoardSinglePlayer(playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)) {
                for (index in 0..99)
                    BoardSquare(index, squareSizeDp, squareSizePx, playerGridViewModel, true, ) {
                    }
            }
        }
    } else {
        Row() {
            GameBoardSinglePlayer(playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)) {
                for (index in 0..99)
                    BoardSquare(index, squareSizeDp, squareSizePx, playerGridViewModel, true) {
                    }
            }
        }
    }
}

