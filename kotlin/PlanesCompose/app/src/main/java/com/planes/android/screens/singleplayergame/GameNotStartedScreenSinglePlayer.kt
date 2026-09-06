package com.planes.android.screens.singleplayergame

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.singleplayerengine.SinglePlayerRoundInterface
import com.planes.singleplayerengine.RoundEndStatus

@Composable
fun GameNotStartedScreenSinglePlayer(modifier: Modifier, currentTitleState: MutableState<String>,
                                     currentScreenState: MutableState<String>,
                                     showPopupState: MutableState<Boolean>,
                                     navController: NavController,
                                     planeRound: SinglePlayerRoundInterface,
                                     playerGridViewModel: PlayerGridViewModelSinglePlayer,
                                     computerGridViewModel: ComputerGridViewModelSinglePlayer
) {

    currentTitleState.value = stringResource(R.string.game)
    currentScreenState.value = PlanesScreens.SinglePlayerGameNotStarted.name
    showPopupState.value = false

    val configuration = LocalConfiguration.current
    val containerSize = LocalWindowInfo.current.containerSize
    val screenWidthDp = with(LocalDensity.current) { containerSize.width.toDp() }
    val screenHeightDp = with(LocalDensity.current) { containerSize.height.toDp() }
    var squareSizeDp = screenWidthDp / playerGridViewModel.getColNo()

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        squareSizeDp = screenHeightDp / playerGridViewModel.getRowNo()
    }

    val boardSizeDp = squareSizeDp * playerGridViewModel.getRowNo()
    val squareSizePx = with(LocalDensity.current) { squareSizeDp.toPx() }

    //Log.d("Planes", "planes no ${planesGridViewModel.getPlaneNo()}")

    var refButtonHeightDp = (screenHeightDp - boardSizeDp) / 4

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        refButtonHeightDp = screenHeightDp / 4
    }

    var refButtonWidthDp = screenWidthDp / 3

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        refButtonWidthDp = (screenWidthDp - boardSizeDp) / 3
    }

    val playerBoard = rememberSaveable {
        mutableStateOf(false)
    }

    val gameBoardViewModel = if (playerBoard.value) playerGridViewModel else computerGridViewModel

    val titleOtherBoard1 = if (playerBoard.value) stringResource(R.string.view_computer_board1) else stringResource(R.string.view_player_board1)
    val titleOtherBoard2 = if (playerBoard.value) stringResource(R.string.view_computer_board2) else stringResource(R.string.view_player_board2)

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column {
            GameBoardSinglePlayer(gameBoardViewModel.getRowNo(), gameBoardViewModel.getColNo(),
                modifier = Modifier.width(boardSizeDp)
                    .height(boardSizeDp)) {
                for (index in 0..99)
                    BoardSquareGameNotStarted(index, squareSizeDp, squareSizePx, gameBoardViewModel)
            }

            Column(modifier = Modifier.height(screenHeightDp - boardSizeDp),
                verticalArrangement = Arrangement.Center) {
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp).fillMaxWidth()) {
                    TwoLineGameButton(
                        textLine1 = titleOtherBoard1,
                        textLine2 = titleOtherBoard2,
                        gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp * 2 / 3).height(refButtonHeightDp),
                        enabled = true
                    ) {
                        playerBoard.value = !playerBoard.value
                    }
                    Column {

                        val winnerTitle: String = when(planeRound.getRoundEndStatus()) {
                            RoundEndStatus.PlayerWins -> stringResource(R.string.player_winner)
                            RoundEndStatus.ComputerWins -> stringResource(R.string.computer_winner)
                            RoundEndStatus.Draw -> stringResource(R.string.draw_result)
                            RoundEndStatus.Cancelled -> stringResource(R.string.round_cancelled)
                        }
                        OneLineGameButton(
                            textLine = winnerTitle, gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp * 4 / 3)
                                .height(refButtonHeightDp / 2),
                            enabled = true
                        ) {

                        }

                        Row {
                            OneLineGameButton(
                                textLine = stringResource(R.string.computer_wins), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp)
                                    .height(refButtonHeightDp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.playerGuess_StatNoComputerWins(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp / 3)
                                    .height(refButtonHeightDp / 2),
                                hot = false)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp).fillMaxWidth()) {
                    TwoLineGameButton(
                        textLine1 = stringResource(R.string.start_new_game1),
                        textLine2 = stringResource(R.string.start_new_game2),
                        gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp * 2 / 3).height(refButtonHeightDp),
                        enabled = true
                    ) {
                        planeRound.initRound()
                        playerGridViewModel.resetFromPlaneRound()
                        computerGridViewModel.resetFromPlaneRound()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.SinglePlayerBoardEditing.name)
                    }
                    Column {
                        Row {
                            OneLineGameButton(
                                textLine = stringResource(R.string.player_wins), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp)
                                    .height(refButtonHeightDp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.playerGuess_StatNoPlayerWins(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp / 3)
                                    .height(refButtonHeightDp / 2),
                                hot = false)
                        }

                        Row {
                            OneLineGameButton(
                                textLine = stringResource(R.string.draws), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp)
                                    .height(refButtonHeightDp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.playerGuess_StatNoDraws(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp / 3)
                                    .height(refButtonHeightDp / 2),
                                hot = false)
                        }
                    }
                }
            }
        }
    } else { //landscape
        Row {
            GameBoardSinglePlayer(gameBoardViewModel.getRowNo(), gameBoardViewModel.getColNo(),
                modifier = Modifier.width(boardSizeDp)
                    .height(boardSizeDp)) {
                for (index in 0..99)
                    BoardSquareGameNotStarted(index, squareSizeDp, squareSizePx, gameBoardViewModel)
            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.height(boardSizeDp)
                        .width(refButtonWidthDp * 2 / 3),
                    verticalArrangement = Arrangement.Center
                ) {
                    TwoLineGameButton(
                        textLine1 = titleOtherBoard1,
                        textLine2 = titleOtherBoard2,
                        gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp * 2 / 3).height(refButtonHeightDp),
                        enabled = true
                    ) {
                        playerBoard.value = !playerBoard.value
                    }
                    TwoLineGameButton(
                        textLine1 = stringResource(R.string.start_new_game1),
                        textLine2 = stringResource(R.string.start_new_game2),
                        gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp * 2 / 3).height(refButtonHeightDp),
                        enabled = true
                    ) {
                        planeRound.initRound()
                        playerGridViewModel.resetFromPlaneRound()
                        computerGridViewModel.resetFromPlaneRound()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.SinglePlayerBoardEditing.name)
                    }
                }

                Column( Modifier.height(boardSizeDp)
                    .width(refButtonWidthDp * 4 / 3),
                    verticalArrangement = Arrangement.Center) {

                    val winnerTitle: String = when(planeRound.getRoundEndStatus()) {
                        RoundEndStatus.PlayerWins -> stringResource(R.string.player_wins)
                        RoundEndStatus.ComputerWins -> stringResource(R.string.computer_winner)
                        RoundEndStatus.Draw -> stringResource(R.string.draw_result)
                        RoundEndStatus.Cancelled -> stringResource(R.string.round_cancelled)
                    }

                    OneLineGameButton(
                        textLine = winnerTitle, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp * 4 / 3)
                            .height(refButtonHeightDp / 2),
                        enabled = true
                    ) {

                    }

                    Row {
                        OneLineGameButton(
                            textLine = stringResource(R.string.computer_wins), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp)
                                .height(refButtonHeightDp / 2),
                            enabled = true
                        ) {

                        }

                        StatsValueField(value = planeRound.playerGuess_StatNoComputerWins(),
                            enabled = true,
                            modifier = Modifier.width(refButtonWidthDp / 3)
                                .height(refButtonHeightDp / 2),
                            hot = false)
                    }

                    Row {
                        OneLineGameButton(
                            textLine = stringResource(R.string.player_wins), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp)
                                .height(refButtonHeightDp / 2),
                            enabled = true
                        ) {

                        }

                        StatsValueField(value = planeRound.playerGuess_StatNoPlayerWins(),
                            enabled = true,
                            modifier = Modifier.width(refButtonWidthDp / 3)
                                .height(refButtonHeightDp / 2),
                            hot = false)
                    }

                    Row {
                        OneLineGameButton(
                            textLine = stringResource(R.string.draws), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp)
                                .height(refButtonHeightDp / 2),
                            enabled = true
                        ) {

                        }

                        StatsValueField(value = planeRound.playerGuess_StatNoDraws(),
                            enabled = true,
                            modifier = Modifier.width(refButtonWidthDp / 3)
                                .height(refButtonHeightDp / 2),
                            hot = false)
                    }
                }
            }
        }
    }
}

