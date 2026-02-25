package com.planes.android.screens.multiplayergame

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.singleplayergame.BoardSquareGame
import com.planes.android.screens.singleplayergame.ComputerGridViewModelSinglePlayer
import com.planes.android.screens.singleplayergame.GameBoardSinglePlayer
import com.planes.android.screens.singleplayergame.GameStatsViewModelSinglePlayer
import com.planes.android.screens.singleplayergame.OneLineGameButton
import com.planes.android.screens.singleplayergame.PlayerGridViewModelSinglePlayer
import com.planes.android.screens.singleplayergame.StatsValueField
import com.planes.android.screens.singleplayergame.TwoLineGameButton
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.GuessPoint
import com.planes.singleplayerengine.SinglePlayerRoundInterface
import com.planes.singleplayerengine.Type

@Composable
fun GameScreenMultiPlayer(modifier: Modifier, currentScreenState: MutableState<String>,
                          topBarHeight: MutableState<Int>,
                          navController: NavController,
                          planeRound: MultiPlayerRoundInterface,
                          playerGridViewModel: PlayerGridViewModelMultiPlayer,
                          computerGridViewModel: ComputerGridViewModelMultiPlayer,
                          gameStatsViewModelMultiPlayer: GameStatsViewModelMultiPlayer
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
    val squareSizePx = with(LocalDensity.current) { squareSizeDp.dp.toPx() }

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

    val titleOtherBoard1 = if (playerBoard.value) stringResource(R.string.view_computer_board1) else stringResource(
        R.string.view_player_board1)
    val titleOtherBoard2 = if (playerBoard.value) stringResource(R.string.view_computer_board2) else stringResource(
        R.string.view_player_board2)

    val titleStats = if (!playerBoard.value) stringResource(R.string.computer_stats)
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
                        gameBoardViewModel
                    ) {
                        if (gameBoardViewModel.isComputer()) {
                            val row = index / gameBoardViewModel.getColNo()
                            val col = index % gameBoardViewModel.getColNo()

                            Log.d("Planes", "Guess at $row and $col")

                            //TODO: and if not all planes discovered or game end
                            if (!planeRound.playerGuessAlreadyMade(row, col)) {
                                planeRound.playerGuess(row, col)
                                val pgp = GuessPoint(col, row, planeRound.playerGuess_GuessResult())
                                computerGridViewModel.addGuess(pgp)

                                gameStatsViewModelMultiPlayer.updateFromPlaneRound()
                                computerGridViewModel.sendMoves()
                            }
                        }
                    }
            }

            val lastMove = if (!playerBoard.value)
                gameStatsViewModelMultiPlayer.getLastComputerMove()
            else
                gameStatsViewModelMultiPlayer.getLastPlayerMove()

            Column(modifier = Modifier.height(screenHeightDp.dp - boardSizeDp.dp),
                verticalArrangement = Arrangement.Center) {
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp.dp / 2).fillMaxWidth()) {
                    Spacer(
                        modifier = Modifier.width(refButtonWidthDp.dp)
                            .height(refButtonHeightDp.dp / 2)
                    )
                    OneLineGameButton(
                        textLine = titleStats, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {

                    }
                }
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp.dp).fillMaxWidth()) {
                    TwoLineGameButton(
                        textLine1 = titleOtherBoard1,
                        textLine2 = titleOtherBoard2,
                        gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        //TODO: move received moves from PlaneRound to playerGridViewModel
                        if (playerBoard.value == false)
                            playerGridViewModel.updateGuessesFromPlaneRound()
                        playerBoard.value = !playerBoard.value
                    }
                    Column() {
                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_moves), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }
                            StatsValueField(value = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerMoves()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerMoves(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = false)
                        }

                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_misses), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            val misses = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerMisses()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerMisses()

                            val hotMisses = (lastMove == Type.Miss) && (misses > 0)

                            StatsValueField(value = misses,
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = hotMisses)
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.height(refButtonHeightDp.dp).fillMaxWidth()) {
                    OneLineGameButton(
                        textLine = stringResource(R.string.cancel), gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        planeRound.cancelRound()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
                    }
                    Column(modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_hits), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            val hits = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerHits()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerHits()

                            val hotHits = (lastMove == Type.Hit) && (hits > 0)

                            StatsValueField(value = hits,
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = hotHits)
                        }

                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_dead), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            val dead = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerDead()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerDead()

                            val hotDead = (lastMove == Type.Dead) && (dead > 0)

                            StatsValueField(value = dead,
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = hotDead)
                        }
                    }
                }
            }
        }
    } else {  //landscape
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
                        gameBoardViewModel
                    ) {
                        if (gameBoardViewModel.isComputer()) {
                            val row = index / gameBoardViewModel.getColNo()
                            val col = index % gameBoardViewModel.getColNo()

                            Log.d("Planes", "Guess at $row and $col")

                            if (!planeRound.playerGuessAlreadyMade(row, col)) {
                                planeRound.playerGuess(row, col)
                                val gp = GuessPoint(col, row, planeRound.playerGuess_GuessResult())
                                gameBoardViewModel.addGuess(gp)

                                gameStatsViewModelMultiPlayer.updateFromPlaneRound()
                            }
                        }
                    }
            }

            val lastMove = if (!playerBoard.value)
                gameStatsViewModelMultiPlayer.getLastComputerMove()
            else
                gameStatsViewModelMultiPlayer.getLastPlayerMove()

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
                    TwoLineGameButton(
                        textLine1 = titleOtherBoard1,
                        textLine2 = titleOtherBoard2,
                        gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        //TODO: move received moves from PlaneRound to playerGridViewModel
                        if (playerBoard.value == false)
                            playerGridViewModel.updateGuessesFromPlaneRound()
                        playerBoard.value = !playerBoard.value
                    }
                    OneLineGameButton(
                        textLine = stringResource(R.string.cancel), gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp),
                        enabled = true
                    ) {
                        planeRound.cancelRound()
                        navController.popBackStack()
                        navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
                    }
                }

                Column(modifier = Modifier.padding(top = topBarHeight.value.dp)
                    .height(boardSizeDp.dp)
                    .width(refButtonWidthDp.dp),
                    verticalArrangement = Arrangement.Center) {
                    OneLineGameButton(
                        textLine = titleStats, gameBoardViewModel,
                        modifier = Modifier.width(refButtonWidthDp.dp).height(refButtonHeightDp.dp / 2),
                        enabled = true
                    ) {

                    }
                    Column() {
                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_moves), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }
                            StatsValueField(value = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerMoves()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerMoves(),
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = false)
                        }

                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_misses), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            val misses = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerMisses()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerMisses()

                            val hotMisses = (lastMove == Type.Miss) && (misses > 0)

                            StatsValueField(value = misses,
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = hotMisses)
                        }
                    }

                    Column() {
                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_hits), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            val hits = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerHits()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerHits()

                            val hotHits = (lastMove == Type.Hit) && (hits > 0)

                            StatsValueField(value = hits,
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = hotHits)
                        }
                        Row() {
                            OneLineGameButton(
                                textLine = stringResource(R.string.general_dead), gameBoardViewModel,
                                modifier = Modifier.width(refButtonWidthDp.dp * 3 / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                enabled = true
                            ) {

                            }

                            val dead = if (!playerBoard.value)
                                gameStatsViewModelMultiPlayer.getComputerDead()
                            else
                                gameStatsViewModelMultiPlayer.getPlayerDead()

                            val hotDead = (lastMove == Type.Dead) && (dead > 0)

                            StatsValueField(value = dead,
                                enabled = true,
                                modifier = Modifier.width(refButtonWidthDp.dp / 4)
                                    .height(refButtonHeightDp.dp / 2),
                                hot = hotDead)
                        }
                    }
                }
            }
        }
    }
}

