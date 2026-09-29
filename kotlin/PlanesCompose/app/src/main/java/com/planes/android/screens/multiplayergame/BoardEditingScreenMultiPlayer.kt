package com.planes.android.screens.multiplayergame

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.createmultiplayergame.CreateGameStates
import com.planes.android.screens.createmultiplayergame.CreateViewModel
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.singleplayergame.BoardEditingControlButtonsHorizontalLayout
import com.planes.android.screens.singleplayergame.BoardEditingControlButtonsVerticalLayout
import com.planes.android.screens.singleplayergame.BoardSquareBoardEditing
import com.planes.android.screens.singleplayergame.GameBoardSinglePlayer
import com.planes.android.screens.singleplayergame.OneLineGameButton
import com.planes.android.screens.singleplayergame.treatSwipeHorizontal
import com.planes.android.screens.singleplayergame.treatSwipeVertical
import java.util.Date

@Composable
fun BoardEditingScreenMultiPlayer(modifier: Modifier, currentTitleState: MutableState<String>,
                                  currentScreenState: MutableState<String>,
                                  showPopupState: MutableState<Boolean>,
                                  navController: NavController,
                                  loginViewModel: LoginViewModel,
                                  createViewModel: CreateViewModel,
                                  playerGridViewModel: PlayerGridViewModelMultiPlayer,
                                  computerGridViewModel: ComputerGridViewModelMultiPlayer
) {

    currentTitleState.value = stringResource(R.string.game)
    currentScreenState.value = PlanesScreens.MultiplayerBoardEditing.name
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

    var buttonHeightDp = (screenHeightDp - boardSizeDp) / 4

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        buttonHeightDp = screenHeightDp / 4
    }

    var buttonWidthDp = screenWidthDp / 3

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        buttonWidthDp = (screenWidthDp - boardSizeDp) / 3
    }

    val squareSizePx = with(LocalDensity.current) { squareSizeDp.toPx() }
    val swipeThresh = 20.0f
    val consecSwipeThresh = 100
    var swipeLengthX = 0.0f
    var swipeLengthY = 0.0f
    var curTime = Date()


    //Log.d("Planes", "planes no ${planesGridViewModel.getPlaneNo()}")

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column {

            if (!loginViewModel.isLoggedIn() || !createViewModel.gameConnectionExists()) {

                //TODO: button connect to game

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
                            modifier = Modifier
                        )
                    }
                }
            } else {
                GameBoardSinglePlayer(
                    playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                    modifier = Modifier.width(boardSizeDp)
                        .height(boardSizeDp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { _ -> playerGridViewModel.rotatePlane(playerGridViewModel.getSelectedPlane()) }
                            )}
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
                            playerGridViewModel) {
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

                    if (createViewModel.getGameNameState().value != computerGridViewModel.getGameName()) {
                        playerGridViewModel.setCredentials(
                            loginViewModel.getLoggedInTokenState(),
                            createViewModel.getGameNameState(),
                            createViewModel.getGameIdState(),
                            createViewModel.getCurrentRoundIdState(),
                            loginViewModel.getLoggedInUsernameState(),
                            loginViewModel.getLoggedInUserIdState(),
                            otherPlayerIdState
                        )
                        computerGridViewModel.setCredentials(
                            loginViewModel.getLoggedInTokenState(),
                            createViewModel.getGameNameState(),
                            createViewModel.getGameIdState(),
                            createViewModel.getCurrentRoundIdState(),
                            loginViewModel.getLoggedInUsernameState(),
                            loginViewModel.getLoggedInUserIdState(),
                            otherPlayerIdState
                        )
                    } else {
                        val roundId = computerGridViewModel.getRoundId()
                        playerGridViewModel.setRoundId(roundId)
                        createViewModel.setCurrentRoundId("Connect", roundId)
                        createViewModel.setCurrentRoundId("Create", roundId)
                    }
                    BoardEditingControlButtonsVerticalLayout(
                        screenHeightDp, boardSizeDp, buttonHeightDp,
                        buttonWidthDp, navController,
                        playerGridViewModel,
                        !playerGridViewModel.isPlaneOutsideGrid() && !playerGridViewModel.doPlanesOverlap() &&
                                (createViewModel.getCreateState() == CreateGameStates.ConnectedComplete || createViewModel.getCreateState() == CreateGameStates.PollingForConnectionEnded)
                    )
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.Cancel) {
                    playerGridViewModel.cancelRound()
                    navController.popBackStack()
                    navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
                    //navController.navigate(route = PlanesScreens.MultiplayerGame.name)
                    //TODO: toast
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.OpponentPlanePositionsReceived) {
                    Log.d("Planes", "ComputerGridViewModel prepare for game starts")
                    computerGridViewModel.prepareForGame(playerGridViewModel.getReceivedPlaneList())
                    Log.d("Planes", "ComputerGridViewModel prepare for game ends")
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
        Row {
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
                            modifier = Modifier
                        )
                    }
                }
            } else {
                GameBoardSinglePlayer(
                    playerGridViewModel.getRowNo(), playerGridViewModel.getColNo(),
                    modifier = Modifier.width(boardSizeDp)
                        .height(boardSizeDp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { _ -> playerGridViewModel.rotatePlane(playerGridViewModel.getSelectedPlane()) }
                            )}
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
                            playerGridViewModel) {
                            val row = index / playerGridViewModel.getColNo()
                            val col = index % playerGridViewModel.getColNo()

                            playerGridViewModel.setSelectedPlane(row, col)
                        }
                }

                if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.EditPlanePositions) {

                    //TODO: stack this into a function
                    var otherPlayerIdState = createViewModel.getSecondPlayerIdState()
                    if (otherPlayerIdState.value == loginViewModel.getLoggedInUserIdState().value) {
                        otherPlayerIdState = createViewModel.getFirstPlayerIdState()
                    }

                    if (createViewModel.getGameNameState().value != computerGridViewModel.getGameName()) {
                        playerGridViewModel.setCredentials(
                            loginViewModel.getLoggedInTokenState(),
                            createViewModel.getGameNameState(),
                            createViewModel.getGameIdState(),
                            createViewModel.getCurrentRoundIdState(),
                            loginViewModel.getLoggedInUsernameState(),
                            loginViewModel.getLoggedInUserIdState(),
                            otherPlayerIdState
                        )
                        computerGridViewModel.setCredentials(
                            loginViewModel.getLoggedInTokenState(),
                            createViewModel.getGameNameState(),
                            createViewModel.getGameIdState(),
                            createViewModel.getCurrentRoundIdState(),
                            loginViewModel.getLoggedInUsernameState(),
                            loginViewModel.getLoggedInUserIdState(),
                            otherPlayerIdState
                        )
                    } else {
                        val roundId = computerGridViewModel.getRoundId()
                        playerGridViewModel.setRoundId(roundId)
                        createViewModel.setCurrentRoundId("Connect", roundId)
                        createViewModel.setCurrentRoundId("Create", roundId)
                    }
                    BoardEditingControlButtonsHorizontalLayout(buttonHeightDp,
                        buttonWidthDp,  navController,
                        playerGridViewModel,
                        !playerGridViewModel.isPlaneOutsideGrid() && !playerGridViewModel.doPlanesOverlap() &&
                                (createViewModel.getCreateState() == CreateGameStates.ConnectedComplete || createViewModel.getCreateState() == CreateGameStates.PollingForConnectionEnded)
                    )
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.Cancel) {
                    playerGridViewModel.cancelRound()
                    navController.popBackStack()
                    navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
                    //navController.navigate(route = PlanesScreens.MultiplayerGame.name)
                    //TODO: toast
                } else if (playerGridViewModel.getBoardEditingState() == BoardEditingStates.OpponentPlanePositionsReceived) {
                    Log.d("Planes", "ComputerGridViewModel prepare for game starts")
                    computerGridViewModel.prepareForGame(playerGridViewModel.getReceivedPlaneList())
                    Log.d("Planes", "ComputerGridViewModel prepare for game ends")
                    //TODO: toast
                    navController.popBackStack()
                    navController.navigate(route = PlanesScreens.MultiplayerGame.name)
                } else {
                    TransferPlanePositionsHorizontalLayout(
                        screenWidthDp, boardSizeDp, buttonHeightDp,
                        buttonWidthDp,  navController,
                        playerGridViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TransferPlanePositionsVerticalLayout(
    screenHeightDp: Dp, boardSizeDp: Dp, buttonHeightDp: Dp,
    buttonWidthDp: Dp, navController: NavController,
    playerGridViewModel: PlayerGridViewModelMultiPlayer
                                         ) {
    Column(
        modifier = Modifier.height(screenHeightDp - boardSizeDp),
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
                modifier = Modifier.width((buttonWidthDp * 2)).height(buttonHeightDp),
                enabled = true
            ) {
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            CircularProgressIndicator(
                modifier = Modifier.width(buttonWidthDp)
                    .height(buttonHeightDp)
                    .padding(buttonWidthDp / 4, buttonHeightDp / 4),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            OneLineGameButton(
                textLine = stringResource(R.string.cancel), playerGridViewModel,
                modifier = Modifier.width(buttonWidthDp).height(buttonHeightDp),
                enabled = true
            ) {
                playerGridViewModel.cancelRound()
                navController.popBackStack()
                navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
            }
        }
    }
}

@Composable
fun TransferPlanePositionsHorizontalLayout(
    screenWidthDp: Dp, boardSizeDp: Dp, buttonHeightDp: Dp,
    buttonWidthDp: Dp, navController: NavController,
    playerGridViewModel: PlayerGridViewModelMultiPlayer
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
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
                modifier = Modifier.width(buttonWidthDp * 2).height(buttonHeightDp),
                enabled = true
            ) {
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            CircularProgressIndicator(
                modifier = Modifier.width(buttonWidthDp)
                    .height(buttonHeightDp)
                    .padding(buttonWidthDp / 4, buttonHeightDp / 4),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            OneLineGameButton(
                textLine = stringResource(R.string.cancel), playerGridViewModel,
                modifier = Modifier.width(buttonWidthDp).height(buttonHeightDp),
                enabled = true
            ) {
                playerGridViewModel.cancelRound()
                navController.popBackStack()
                navController.navigate(route = PlanesScreens.MultiplayerGameNotStarted.name)
            }
        }
    }
}