package com.planes.android.screens.createmultiplayergame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.widgets.CommonTextFieldWithViewModel

@Composable
fun CreateMultiplayerGameScreen(modifier: Modifier,
                          currentScreenState: MutableState<String>,
                          navController: NavController,
                          loginViewModel: LoginViewModel,
                          createViewModel: CreateViewModel
) {

    currentScreenState.value = PlanesScreens.CreateMultiplayerGame.name
    val submitClickedState = rememberSaveable {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (!loginViewModel.isLoggedIn()) {
            Text(text = stringResource(R.string.nouser))
        } else {
            CommonTextFieldWithViewModel(modifier = Modifier.padding(15.dp),
                createViewModel,
                { create ->  create.getGameName()},
                { create, str -> create.setGameName(str)},
                onAction = KeyboardActions {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
                placeholder = stringResource(R.string.game_name)
            )

            Button(modifier = Modifier.padding(15.dp),
                onClick = {
                    createViewModel.gameStatus(loginViewModel.getLoggedInToken()!!, loginViewModel.getLoggedInUserId()!!, loginViewModel.getLoggedInUserName()!!)
                }) {
                Text(text = stringResource(R.string.submit))
            }
        }
    }
}