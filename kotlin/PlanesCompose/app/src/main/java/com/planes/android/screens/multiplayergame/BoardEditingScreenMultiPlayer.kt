package com.planes.android.screens.multiplayergame

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.createmultiplayergame.CreateViewModel
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.singleplayergame.BoardEditingControlButtonsHorizontalLayout
import com.planes.android.screens.singleplayergame.BoardEditingControlButtonsVerticalLayout
import com.planes.android.screens.singleplayergame.BoardSquareBoardEditing
import com.planes.android.screens.singleplayergame.GameBoardSinglePlayer
import com.planes.android.screens.singleplayergame.OneLineGameButton
import com.planes.android.screens.singleplayergame.PlaneGridViewModel
import com.planes.android.screens.singleplayergame.PlayerGridViewModelSinglePlayer
import com.planes.android.screens.singleplayergame.treatSwipeHorizontal
import com.planes.android.screens.singleplayergame.treatSwipeVertical
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.SinglePlayerRoundInterface
import java.util.Date

@Composable
fun BoardEditingScreenMultiPlayer(modifier: Modifier, currentScreenState: MutableState<String>,
                                  topBarHeight: MutableState<Int>,
                                  navController: NavController,
                                  loginViewModel: LoginViewModel,
                                  createViewModel: CreateViewModel,
                                  planeRound: MultiPlayerRoundInterface,
                                  playerGridViewModel: PlayerGridViewModelMultiPlayer,
                                  computerGridViewModel: ComputerGridViewModelMultiPlayer
) {

    currentScreenState.value = PlanesScreens.MultiplayerBoardEditing.name

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

            if (!loginViewModel.isLoggedIn() || !createViewModel.gameConnectionExists()) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var errorText = stringResource(R.string.nouser)

                        if (loginViewModel.isLoggedIn()) {
                            errorText = stringResource(R.string.validation_not_connected_to_game)
                        }

                        Text(
                            text = errorText,
                            modifier = Modifier.padding(top = topBarHeight.value.dp)
                        )
                    }
                }
            } else {
                GameBoardSinglePlayer(
                    playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                    modifier = Modifier.padding(top = topBarHeight.value.dp)
                        .width(boardSizeDp.dp).height(boardSizeDp.dp)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { _, dragAmount ->
                                    val tripleVal = treatSwipeVertical(
                                        swipeThresh,
                                        consecSwipeThresh,
                                        swipeLengthX,
                                        swipeLengthY,
                                        squareSizePx,
                                        curTime,
                                        dragAmount,
                                        playerGridViewModel
                                    )
                                    swipeLengthX = tripleVal.first
                                    swipeLengthY = tripleVal.second
                                    curTime = tripleVal.third
                                }
                            )
                        }) {
                    for (index in 0..99)
                        BoardSquareBoardEditing(
                            index,
                            squareSizeDp,
                            squareSizePx,
                            playerGridViewModel
                        ) {
                            val row = index / playerGridViewModel.getColNo()
                            val col = index % playerGridViewModel.getColNo()

                            playerGridViewModel.setSelectedPlane(row, col)
                        }
                }

                if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.EditPlanePositions) {

                    var otherPlayerIdState = createViewModel.getSecondPlayerIdState()
                    if (otherPlayerIdState.value == loginViewModel.getLoggedInUserIdState().value) {
                        otherPlayerIdState = createViewModel.getFirstPlayerIdState()
                    }

                    playerGridViewModel.setCredentials(loginViewModel.getLoggedInTokenState(),
                        createViewModel.getGameNameState(), createViewModel.getGameIdState(),
                        createViewModel.getCurrentRoundIdState(), loginViewModel.getLoggedInUsernameState(),
                        loginViewModel.getLoggedInUserIdState(), otherPlayerIdState
                        )
                    computerGridViewModel.setCredentials(loginViewModel.getLoggedInTokenState(),
                        createViewModel.getGameNameState(), createViewModel.getGameIdState(),
                        createViewModel.getCurrentRoundIdState(), loginViewModel.getLoggedInUsernameState(),
                        loginViewModel.getLoggedInUserIdState(), otherPlayerIdState
                    )
                    BoardEditingControlButtonsVerticalLayout(
                        screenHeightDp, boardSizeDp, buttonHeightDp,
                        buttonWidthDp, navController,
                        playerGridViewModel,
                        planeRound
                    )
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.Cancel) {
                    playerGridViewModel.cancelRound()
                    navController.popBackStack()
                    navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
                    //TODO: toast
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.OpponentPlanePositionsReceived) {
                    //TODO: to optimize this
                    computerGridViewModel.resetGrid()
                    val receivedPlanes = playerGridViewModel.getReceivedPlaneList()
                    receivedPlanes.forEach {

                        //Log.d("Planes", "Plane  ${it.row()},${it.col} with ${it.orientation().value}")
                        computerGridViewModel.savePlane(it)
                    }
                    computerGridViewModel.computePlanePointsList()
                    computerGridViewModel.updatePlanesToPlaneRound()
                    //TODO: toast
                    navController.popBackStack()
                    navController.navigate(route = PlanesScreens.MultiplayerGame.name)
                } else {
                    TransferPlanePositionsVerticalLayout(
                        screenHeightDp, boardSizeDp, buttonHeightDp,
                        buttonWidthDp, navController,
                        playerGridViewModel
                    )
                }
            }
        }
    } else {  //landscape
        Row() {
            if (!loginViewModel.isLoggedIn() || !createViewModel.gameConnectionExists()) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        var errorText = stringResource(R.string.nouser)

                        if (loginViewModel.isLoggedIn()) {
                            errorText = stringResource(R.string.validation_not_connected_to_game)
                        }
                        Text(
                            text = errorText,
                            modifier = Modifier.padding(top = topBarHeight.value.dp)
                        )
                    }
                }
            } else {
                GameBoardSinglePlayer(
                    playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                    modifier = Modifier.padding(top = topBarHeight.value.dp)
                        .width(boardSizeDp.dp).height(boardSizeDp.dp)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { _, dragAmount ->
                                    val tripleVal = treatSwipeHorizontal(
                                        swipeThresh,
                                        consecSwipeThresh,
                                        swipeLengthX,
                                        swipeLengthY,
                                        squareSizePx,
                                        curTime,
                                        dragAmount,
                                        playerGridViewModel
                                    )
                                    swipeLengthX = tripleVal.first
                                    swipeLengthY = tripleVal.second
                                    curTime = tripleVal.third
                                })
                        }) {
                    for (index in 0..99)
                        BoardSquareBoardEditing(
                            index,
                            squareSizeDp,
                            squareSizePx,
                            playerGridViewModel
                        ) {
                            val row = index / playerGridViewModel.getColNo()
                            val col = index % playerGridViewModel.getColNo()

                            playerGridViewModel.setSelectedPlane(row, col)
                        }
                }

                if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.EditPlanePositions) {

                    var otherPlayerIdState = createViewModel.getSecondPlayerIdState()
                    if (otherPlayerIdState.value == loginViewModel.getLoggedInUserIdState().value) {
                        otherPlayerIdState = createViewModel.getFirstPlayerIdState()
                    }

                    playerGridViewModel.setCredentials(loginViewModel.getLoggedInTokenState(),
                        createViewModel.getGameNameState(), createViewModel.getGameIdState(),
                        createViewModel.getCurrentRoundIdState(), loginViewModel.getLoggedInUsernameState(),
                        loginViewModel.getLoggedInUserIdState(), otherPlayerIdState
                    )
                    computerGridViewModel.setCredentials(loginViewModel.getLoggedInTokenState(),
                        createViewModel.getGameNameState(), createViewModel.getGameIdState(),
                        createViewModel.getCurrentRoundIdState(), loginViewModel.getLoggedInUsernameState(),
                        loginViewModel.getLoggedInUserIdState(), otherPlayerIdState
                    )
                    BoardEditingControlButtonsHorizontalLayout(
                        screenHeightDp, boardSizeDp, buttonHeightDp,
                        buttonWidthDp, topBarHeight.value, navController,
                        playerGridViewModel,
                        planeRound
                    )
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.Cancel) {
                    playerGridViewModel.cancelRound()
                    navController.popBackStack()
                    navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
                    //TODO: toast
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.OpponentPlanePositionsReceived) {
                    //TODO: to optimize this

                    computerGridViewModel.resetGrid()
                    val receivedPlanes = playerGridViewModel.getReceivedPlaneList()
                    receivedPlanes.forEach {
                        computerGridViewModel.savePlane(it)
                    }
                    computerGridViewModel.computePlanePointsList()
                    computerGridViewModel.updatePlanesToPlaneRound()

                    //TODO: toast
                    navController.popBackStack()
                    navController.navigate(route = PlanesScreens.MultiplayerGame.name)
                } else {
                    TransferPlanePositionsHorizontalLayout(
                        screenWidthDp, boardSizeDp, buttonHeightDp,
                        buttonWidthDp, topBarHeight.value, navController,
                        playerGridViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TransferPlanePositionsVerticalLayout(screenHeightDp: Int, boardSizeDp: Int, buttonHeightDp: Int,
                                             buttonWidthDp: Int, navController: NavController,
                                             playerGridViewModel: PlayerGridViewModelMultiPlayer
                                         ) {
    Column(
        modifier = Modifier.height(screenHeightDp.dp - boardSizeDp.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            val boardEditingState = playerGridViewModel.getBoardEditingState()
            var infoText = "Send own plane\n positions to opponent"

            if (boardEditingState == BoardEditingStates.WaitForOpponentPlanePositions)
                infoText = "Planes Positions\n sent to Opponent"

            OneLineGameButton(
                textLine = infoText, playerGridViewModel,
                modifier = Modifier.width((buttonWidthDp * 2).dp).height(buttonHeightDp.dp),
                enabled = true
            ) {
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            //TODO: Loader
            OneLineGameButton(
                textLine = "", playerGridViewModel,
                modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                enabled = true
            ) {

            }
            OneLineGameButton(
                textLine = stringResource(R.string.cancel), playerGridViewModel,
                modifier = Modifier.width((buttonWidthDp).dp).height(buttonHeightDp.dp),
                enabled = true
            ) {
                playerGridViewModel.cancelRound()
            }
        }
    }
}

@Composable
fun TransferPlanePositionsHorizontalLayout(screenWidthDp: Int, boardSizeDp: Int, buttonHeightDp: Int,
                                         buttonWidthDp: Int, topBarHeightDp: Int, navController: NavController,
                                         playerGridViewModel: PlayerGridViewModelMultiPlayer
) {
    Column(
        modifier = Modifier.fillMaxHeight()
            .padding(top = topBarHeightDp.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            val boardEditingState = playerGridViewModel.getBoardEditingState()
            var infoText = "Send own plane\n positions to opponent"

            if (boardEditingState == BoardEditingStates.WaitForOpponentPlanePositions)
                infoText = "Planes Positions\n sent to Opponent"

            OneLineGameButton(
                textLine = infoText, playerGridViewModel,
                modifier = Modifier.width((buttonWidthDp * 2).dp).height(buttonHeightDp.dp),
                enabled = true
            ) {
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            //TODO: Loader
            OneLineGameButton(
                textLine = "", playerGridViewModel,
                modifier = Modifier.width(buttonWidthDp.dp).height(buttonHeightDp.dp),
                enabled = true
            ) {

            }
            OneLineGameButton(
                textLine = stringResource(R.string.cancel), playerGridViewModel,
                modifier = Modifier.width((buttonWidthDp).dp).height(buttonHeightDp.dp),
                enabled = true
            ) {
                playerGridViewModel.cancelRound()
            }
        }
    }
}