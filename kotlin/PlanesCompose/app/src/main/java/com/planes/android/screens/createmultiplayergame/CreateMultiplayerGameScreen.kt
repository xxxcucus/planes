package com.planes.android.screens.createmultiplayergame

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.multiplayergame.BoardEditingStates
import com.planes.android.screens.multiplayergame.ComputerGridViewModelMultiPlayer
import com.planes.android.screens.multiplayergame.PlayerGridViewModelMultiPlayer
import com.planes.android.widgets.CommonTextFieldWithViewModel
import com.planes.multiplayerengine.MultiPlayerRoundInterface

@Composable
fun CreateMultiplayerGameScreen(modifier: Modifier,
                          currentScreenState: MutableState<String>,
                          navController: NavController,
                          loginViewModel: LoginViewModel,
                          createViewModel: CreateViewModel,
                          planeRound: MultiPlayerRoundInterface,
                          playerGridViewModel: PlayerGridViewModelMultiPlayer,
                          computerGridViewModel: ComputerGridViewModelMultiPlayer
) {

    currentScreenState.value = PlanesScreens.CreateMultiplayerGame.name

    val keyboardController = LocalSoftwareKeyboardController.current

    //TODO: validation

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (!loginViewModel.isLoggedIn()) {
            Text(text = stringResource(R.string.nouser))
        } else if (createViewModel.getCreateState() == CreateGameStates.StatusNotRequested) {
            CommonTextFieldWithViewModel(
                modifier = Modifier.padding(15.dp),
                createViewModel,
                { create -> create.getGameName() },
                { create, str -> create.setGameName(str) },
                onAction = KeyboardActions {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
                placeholder = stringResource(R.string.game_name)
            )

            Button(
                modifier = Modifier.padding(15.dp),
                onClick = {
                    createViewModel.gameStatus(
                        loginViewModel.getLoggedInToken()!!,
                        loginViewModel.getLoggedInUserId()!!,
                        loginViewModel.getLoggedInUserName()!!
                    )
                }) {
                Text(text = stringResource(R.string.submit))
            }
        } else if (createViewModel.getCreateState() == CreateGameStates.StatusRequested) {
            val error = createViewModel.getError()
            if (createViewModel.getLoading()) {
                Text(text = stringResource(R.string.loader_text))
            } else if (error != null) {
                Toast.makeText(
                    LocalContext.current,
                    createViewModel.getError(),
                    Toast.LENGTH_LONG
                ).show()
                createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
            }
        } else if (createViewModel.getCreateState() == CreateGameStates.StatusReceived) {
            val gameExists = createViewModel.getExists("Status")
            Log.d("PlanesCompose", "Game exists $gameExists")
            if (gameExists == true)
                Log.d(
                    "Planes Compose",
                    "Existing game between ${createViewModel.getFirstPlayerName("Status")} and ${createViewModel.getSecondPlayerName("Status")}"
                )
            if (gameExists == false) {
                Text(text = stringResource(R.string.creategame_possible))
                Row {
                    Button(
                        modifier = Modifier.padding(15.dp),
                        onClick = {
                            createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
                        }) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    Button(
                        modifier = Modifier.padding(15.dp),
                        onClick = {
                            createViewModel.createGame(loginViewModel.getLoggedInToken()!!, loginViewModel.getLoggedInUserId()!!, loginViewModel.getLoggedInUserName()!!)
                        }) {
                        Text(text = stringResource(R.string.create_game))
                    }
                }
            } else if (gameExists == true && createViewModel.getFirstPlayerName("Status") == createViewModel.getSecondPlayerName("Status")) {
                Text(
                    text = LocalContext.current.getString(
                        R.string.connecttogame_possible,
                        createViewModel.getFirstPlayerName("Status")
                    )
                )
                Row {
                    Button(
                        modifier = Modifier.padding(15.dp),
                        onClick = {
                            createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
                        }) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    Button(
                        modifier = Modifier.padding(15.dp),
                        onClick = {
                            createViewModel.connectToGame(loginViewModel.getLoggedInToken()!!, loginViewModel.getLoggedInUserId()!!, loginViewModel.getLoggedInUserName()!!)
                        }) {
                        Text(text = stringResource(R.string.connectto_game))
                    }
                }
            } else if (gameExists == true) {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(R.string.creategame_error),
                    Toast.LENGTH_LONG
                ).show()
                createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
            }
        } else if (createViewModel.getCreateState() == CreateGameStates.ConnectedToGameRequested) {
            val error = createViewModel.getError()
            if (createViewModel.getLoading()) {
                Text(text = stringResource(R.string.loader_text))
            } else if (error != null) {
                Toast.makeText(
                    LocalContext.current,
                    createViewModel.getError(),
                    Toast.LENGTH_LONG
                ).show()
                createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
            }
        } else if (createViewModel.getCreateState() == CreateGameStates.ConnectedComplete) {

            planeRound.initRound()
            playerGridViewModel.resetFromPlaneRound()
            playerGridViewModel.setBoardEditingState(BoardEditingStates.EditPlanePositions)
            computerGridViewModel.resetFromPlaneRound()
            Text(text = LocalContext.current.getString(R.string.connected_togame, createViewModel.getGameName("Connect")))
            Button(
                modifier = Modifier.padding(15.dp),
                onClick = {
                    createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
                }) {
                Text(text = stringResource(R.string.connect_another_game))
            }


        } else if (createViewModel.getCreateState() == CreateGameStates.ConnectedToGameRequested) {
            val error = createViewModel.getError()
            if (createViewModel.getLoading()) {
                Text(text = stringResource(R.string.loader_text))
            } else if (error != null) {
                Toast.makeText(
                    LocalContext.current,
                    createViewModel.getError(),
                    Toast.LENGTH_LONG
                ).show()
                createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
            }
        } else if (createViewModel.getCreateState() == CreateGameStates.GameCreationRequested) {
            val error = createViewModel.getError()
            if (createViewModel.getLoading()) {
                Text(text = stringResource(R.string.loader_text))
            } else if (error != null) {
                Toast.makeText(
                    LocalContext.current,
                    createViewModel.getError(),
                    Toast.LENGTH_LONG
                ).show()
                createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
            }
        }  else if (createViewModel.getCreateState() == CreateGameStates.GameCreationComplete) {
            Text(text = stringResource(R.string.game_created))
        } else if (createViewModel.getCreateState() == CreateGameStates.PollingForConnectionStarted) {
            Text(text = LocalContext.current.getString(R.string.wait_for_opponent, createViewModel.getGameName("Create")))

            Button(
                modifier = Modifier.padding(15.dp),
                onClick = {
                    createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
                }) {
                Text(text = stringResource(R.string.cancel))
            }
        } else if (createViewModel.getCreateState() == CreateGameStates.PollingForConnectionEnded) {
            planeRound.initRound()
            playerGridViewModel.resetFromPlaneRound()
            playerGridViewModel.setBoardEditingState(BoardEditingStates.EditPlanePositions)
            computerGridViewModel.resetFromPlaneRound()
            Text(text = LocalContext.current.getString(R.string.connected_togame, createViewModel.getGameName("Create")))
            Button(
                modifier = Modifier.padding(15.dp),
                onClick = {
                    createViewModel.setCreateState(CreateGameStates.StatusNotRequested)
                }) {
                Text(text = stringResource(R.string.connect_another_game))
            }
        }
    }
}