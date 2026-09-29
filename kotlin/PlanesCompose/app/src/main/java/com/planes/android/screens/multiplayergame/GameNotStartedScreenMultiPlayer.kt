package com.planes.android.screens.multiplayergame

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.singleplayergame.BoardSquareGameNotStarted
import com.planes.android.screens.singleplayergame.GameBoardSinglePlayer
import com.planes.android.screens.singleplayergame.OneLineGameButton
import com.planes.android.screens.singleplayergame.StatsValueField
import com.planes.android.screens.singleplayergame.TwoLineGameButton
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.RoundEndStatus

@Composable
fun GameNotStartedScreenMultiPlayer(modifier: Modifier, currentTitleState: MutableState<String>,
                                    currentScreenState: MutableState<String>,
                                    showPopupState: MutableState<Boolean>,
                                    navController: NavController,
                                    planeRound: MultiPlayerRoundInterface,
                                    playerGridViewModel: PlayerGridViewModelMultiPlayer,
                                    computerGridViewModel: ComputerGridViewModelMultiPlayer
) {

    currentTitleState.value = stringResource(R.string.game)
    currentScreenState.value = PlanesScreens.MultiplayerGameNotStarted.name
    showPopupState.value = false

    val configuration = LocalConfiguration.current
    val containerSize = LocalWindowInfo.current.containerSize
    //val screenWidthDp = with(LocalDensity.current) { containerSize.width.toDp() }
    //val screenHeightDp = with(LocalDensity.current) { containerSize.height.toDp() }

    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

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

    val titleOtherBoard1 = if (playerBoard.value) stringResource(R.string.view_opponent_board1) else stringResource(
        R.string.view_player_board1)
    val titleOtherBoard2 = if (playerBoard.value) stringResource(R.string.view_opponent_board2) else stringResource(
        R.string.view_player_board2)

    if (computerGridViewModel.getStartNewRound()) {
        planeRound.initRound()
        playerGridViewModel.resetFromPlaneRound()
        computerGridViewModel.resetFromPlaneRound()
        computerGridViewModel.resetState()
        playerGridViewModel.resetState()
        navController.popBackStack()
        navController.navigate(route = PlanesScreens.MultiplayerBoardEditing.name)
    }

    //TODO: if not connected to a game, if not logged in

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

                        if (!playerBoard.value)
                            playerGridViewModel.updateGuessesFromPlaneRound()
                        playerBoard.value = !playerBoard.value
                    }
                    Column {

                        val winnerTitle: String = when(planeRound.getRoundEndStatus()) {
                            RoundEndStatus.PlayerWins -> stringResource(R.string.player_winner)
                            RoundEndStatus.ComputerWins -> stringResource(R.string.opponent_winner)
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
                                textLine = stringResource(R.string.opponent_wins), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp)
                                    .height(refButtonHeightDp / 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.stats_NoComputerWins(),
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
                        computerGridViewModel.startNewRound()
                    }
                    Column {
                        Row {
                            OneLineGameButton(
                                textLine = stringResource(R.string.player_wins), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp)
                                    .height(refButtonHeightDp/ 2),
                                enabled = true
                            ) {

                            }

                            StatsValueField(value = planeRound.stats_NoPlayerWins(),
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

                            StatsValueField(value = planeRound.stats_NoDraws(),
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
                    modifier = Modifier
                        .height(boardSizeDp)
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
                        if (!playerBoard.value)
                            playerGridViewModel.updateGuessesFromPlaneRound()
                        playerBoard.value = !playerBoard.value
                    }
                    TwoLineGameButton(
                        textLine1 = stringResource(R.string.start_new_game1),
                        textLine2 = stringResource(R.string.start_new_game2),
                        gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp * 2 / 3).height(refButtonHeightDp),
                        enabled = true
                    ) {
                        computerGridViewModel.startNewRound()
                    }
                }

                Column( Modifier
                    .height(boardSizeDp)
                    .width(refButtonWidthDp * 4 / 3),
                    verticalArrangement = Arrangement.Center) {

                    val winnerTitle: String = when(planeRound.getRoundEndStatus()) {
                        RoundEndStatus.PlayerWins -> stringResource(R.string.player_wins)
                        RoundEndStatus.ComputerWins -> stringResource(R.string.opponent_winner)
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
                            textLine = stringResource(R.string.opponent_wins), gameBoardViewModel,
                            modifier = Modifier.width(refButtonWidthDp)
                                .height(refButtonHeightDp / 2),
                            enabled = true
                        ) {

                        }

                        StatsValueField(value = planeRound.stats_NoComputerWins(),
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

                        StatsValueField(value = planeRound.stats_NoPlayerWins(),
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

                        StatsValueField(value = planeRound.stats_NoDraws(),
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