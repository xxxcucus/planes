package com.planes.android.singleplayergame

import android.content.res.Configuration
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens


@Composable
fun GameScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                       topBarHeight: MutableState<Int>,
                       navController: NavController,
                       playerGridViewModel: PlaneGridViewModel,
                       computerGridViewModel: PlaneGridViewModel,
                        gameStatsViewModel: GameStatsViewModel = hiltViewModel()                        ) {

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

    var refButtonHeightDp = (screenHeightDp - boardSizeDp) / 4

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        refButtonHeightDp = screenHeightDp / 4
    }

    var refButtonWidthDp = screenWidthDp / 3

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        refButtonWidthDp = (screenWidthDp - boardSizeDp) / 3
    }


    //TODO: remember this after screen rotation
    val playerBoard = remember {
        mutableStateOf(false)
    }

    val gameBoardViewModel = if (playerBoard.value) playerGridViewModel else computerGridViewModel

    val titleOtherBoard = if (playerBoard.value) stringResource(R.string.view_computer_board)
    else stringResource(R.string.view_player_board)
    val titleStats = if (playerBoard.value) stringResource(R.string.computer_stats)
    else stringResource(R.string.player_stats)

    //Log.d("Planes", "planes no ${planesGridViewModel.getPlaneNo()}")

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column() {
            GameBoardSinglePlayer(
                gameBoardViewModel.getRowNo(), gameBoardViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)
            ) {
                for (index in 0..99)
                    BoardSquareGame(
                        index,
                        squareSizeDp,
                        squareSizePx,
                        gameBoardViewModel,
                        true
                    ) {
                    }
            }

            Column(modifier = Modifier.height(screenHeightDp.dp - boardSizeDp.dp),
                verticalArrangement = Arrangement.Center) {
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp.dp / 2).fillMaxWidth()) {
                    Spacer(
                        modifier = Modifier.width(refButtonWidthDp.dp)
                            .height(refButtonHeightDp.dp / 2)
                    )
                    GameButton(
                        title = titleStats, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {

                    }
                }
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
                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_moves), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }
                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerMoves()
                            else
                                gameStatsViewModel.getPlayerMoves(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))

                        }

                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_misses), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerMisses()
                            else
                                gameStatsViewModel.getPlayerMisses(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp.dp).fillMaxWidth()) {
                    GameButton(
                        title = stringResource(R.string.cancel), gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {

                    }
                    Column(modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                        ) {

                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_hits), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerHits()
                            else
                                gameStatsViewModel.getPlayerHits(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))
                        }

                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_dead), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerDead()
                            else
                                gameStatsViewModel.getPlayerDead(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))
                        }
                    }
                }
            }
        }
        } else {
        Row() {
            GameBoardSinglePlayer(
                gameBoardViewModel.getRowNo(), gameBoardViewModel.getColNo(),
                modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .width(boardSizeDp.dp).height(boardSizeDp.dp)
            ) {
                for (index in 0..99)
                    BoardSquareGame(
                        index,
                        squareSizeDp,
                        squareSizePx,
                        gameBoardViewModel,
                        true
                    ) {
                    }
            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .height(boardSizeDp.dp)
                    .width(refButtonWidthDp.dp),
                    verticalArrangement = Arrangement.Center) {
                    Spacer(
                        modifier = Modifier.width(refButtonWidthDp.dp)
                            .height(refButtonHeightDp.dp / 2))
                    GameButton(
                        title = titleOtherBoard, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        playerBoard.value = !playerBoard.value
                    }
                    GameButton(
                        title = stringResource(R.string.cancel), gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {

                    }
                }

                Column(modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .height(boardSizeDp.dp)
                    .width(refButtonWidthDp.dp),
                    verticalArrangement = Arrangement.Center) {
                    GameButton(
                        title = titleStats, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp / 2),
                        enabled = true
                    ) {

                    }
                    Column() {
                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_moves), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }
                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerMoves()
                            else
                                gameStatsViewModel.getPlayerMoves(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))
                        }

                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_misses), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }
                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerMisses()
                            else
                                gameStatsViewModel.getPlayerMisses(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))
                        }
                    }

                    Column() {
                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_hits), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }
                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerHits()
                            else
                                gameStatsViewModel.getPlayerHits(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))
                        }
                        Row() {
                            GameButton(
                                title = stringResource(R.string.general_dead), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }
                            StatsValueField(value = if (playerBoard.value)
                                gameStatsViewModel.getComputerDead()
                            else
                                gameStatsViewModel.getPlayerDead(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2))
                        }
                    }
                }
            }
        }

    }
}

