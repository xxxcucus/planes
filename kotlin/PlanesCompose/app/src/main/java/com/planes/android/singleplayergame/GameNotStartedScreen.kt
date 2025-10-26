package com.planes.android.singleplayergame

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.singleplayerengine.PlanesRoundInterface
import com.planes.singleplayerengine.Type

@Composable
fun GameNotStartedScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                         topBarHeight: MutableState<Int>,
                         navController: NavController,
                         planeRound: PlanesRoundInterface,
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

    var refButtonHeightDp = (screenHeightDp - boardSizeDp) / 4

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        refButtonHeightDp = screenHeightDp / 4
    }

    var refButtonWidthDp = screenWidthDp / 3

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        refButtonWidthDp = (screenWidthDp - boardSizeDp) / 3
    }

    val playerBoard = remember {
        mutableStateOf(false)
    }

    val gameBoardViewModel = if (playerBoard.value) playerGridViewModel else computerGridViewModel

    val titleOtherBoard = if (playerBoard.value) stringResource(R.string.view_computer_board)
    else stringResource(R.string.view_player_board)

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column() {
            GameBoardSinglePlayer(gameBoardViewModel.getRowNo(), playerGridViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)) {
                for (index in 0..99)
                    BoardSquareGameNotStarted(index, squareSizeDp, squareSizePx, playerGridViewModel)
            }

            Column(modifier = Modifier.height(screenHeightDp.dp - boardSizeDp.dp),
                verticalArrangement = Arrangement.Center) {
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp.dp).fillMaxWidth()) {
                    GameButton(
                        title = titleOtherBoard, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        playerBoard.value = !playerBoard.value
                    }
                    Column() {

                        GameButton(
                            title = stringResource(R.string.computer_winner), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp.dp)
                                .height(refButtonHeightDp.dp / 2),
                            enabled = true
                        ) {

                        }

                        Row() {
                            GameButton(
                                title = stringResource(R.string.computer_wins), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.playerGuess_StatNoComputerWins(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = false)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp.dp).fillMaxWidth()) {
                    GameButton(
                        title = stringResource(R.string.start_new_game), gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        //TODO:
                    }
                    Column() {
                        Row() {
                            GameButton(
                                title = stringResource(R.string.player_wins), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.playerGuess_StatNoPlayerWins(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = false)
                        }

                        Row() {
                            GameButton(
                                title = stringResource(R.string.draws), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.playerGuess_StatNoDraws(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = false)
                        }
                    }
                }
            }
        }
    } else { //landscape
        Row() {
            GameBoardSinglePlayer(gameBoardViewModel.getRowNo(), playerGridViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)) {
                for (index in 0..99)
                    BoardSquareGameNotStarted(index, squareSizeDp, squareSizePx, playerGridViewModel)
            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.padding(top = topBarHeight.value.dp)
                        .height(boardSizeDp.dp)
                        .width(refButtonWidthDp.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    GameButton(
                        title = titleOtherBoard, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        playerBoard.value = !playerBoard.value
                    }
                    GameButton(
                        title = stringResource(R.string.start_new_game), gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        //TODO:
                    }
                }

                Column( Modifier.padding(top = topBarHeight.value.dp)
                    .height(boardSizeDp.dp)
                    .width(refButtonWidthDp.dp),
                    verticalArrangement = Arrangement.Center) {

                    GameButton(
                        title = stringResource(R.string.computer_winner), gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp)
                            .height(refButtonHeightDp.dp / 2),
                        enabled = true
                    ) {

                    }

                    Row() {
                        GameButton(
                            title = stringResource(R.string.computer_wins), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                .height(refButtonHeightDp.dp / 2),
                            enabled = true
                        ) {

                        }

                        StatsValueField(value = planeRound.playerGuess_StatNoComputerWins(),
                            enabled = true,
                            modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                .height(refButtonHeightDp.dp / 2),
                            hot = false)
                    }

                    Row() {
                        GameButton(
                            title = stringResource(R.string.player_wins), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                .height(refButtonHeightDp.dp / 2),
                            enabled = true
                        ) {

                        }

                        StatsValueField(value = planeRound.playerGuess_StatNoPlayerWins(),
                            enabled = true,
                            modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                .height(refButtonHeightDp.dp / 2),
                            hot = false)
                    }

                    Row() {
                        GameButton(
                            title = stringResource(R.string.draws), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                .height(refButtonHeightDp.dp / 2),
                            enabled = true
                        ) {

                        }

                        StatsValueField(value = planeRound.playerGuess_StatNoDraws(),
                            enabled = true,
                            modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                .height(refButtonHeightDp.dp / 2),
                            hot = false)
                    }
                }
            }
        }
    }
}

