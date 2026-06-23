package com.planes.android.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.chat.ChatUserListViewModel
import com.planes.android.screens.createmultiplayergame.CreateViewModel
import com.planes.android.screens.preferences.PreferencesViewModel
import com.planes.android.widgets.CommonTextFieldWithViewModel
import com.planes.android.widgets.PasswordInputFieldWithViewModel
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(modifier: Modifier, currentTitleState: MutableState<String>,
                currentScreenState: MutableState<String>,
                showPopupState: MutableState<Boolean>,
                userLoginState: MutableState<Boolean>,
                navController: NavController,
                loginViewModel: LoginViewModel,
                chatUserListViewModel: ChatUserListViewModel,
                optionsViewModel: PreferencesViewModel,
                createViewModel: CreateViewModel,
                planeRoundMultiplayer: MultiPlayerRoundInterface,
                autologin: Boolean) {

    LaunchedEffect(Unit) {
        if (autologin) {
            loginViewModel.setPassword(optionsViewModel.getPassword())
            loginViewModel.setUserName(optionsViewModel.getUserName())
            loginViewModel.login()
        }
    }

    currentTitleState.value = stringResource(R.string.login)
    currentScreenState.value  = PlanesScreens.Login.name
    showPopupState.value = false
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val submitClickedState = rememberSaveable {
        mutableStateOf(false)
    }

    val savedCredentialsState = rememberSaveable {
        mutableStateOf(false)
    }

    userLoginState.value = loginViewModel.isLoggedIn()

    //TODO: reset round and other view models when logging in as a different user

    Column(modifier = modifier.fillMaxSize().verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (loginViewModel.isLoggedIn()) {

            if (!savedCredentialsState.value) {
                optionsViewModel.setUserName(loginViewModel.getUserName())
                optionsViewModel.setPassword(loginViewModel.getPassword())
                savedCredentialsState.value = true
            }

            Text(text = stringResource(R.string.userloggedin) + " " + loginViewModel.getLoggedInUserName())

            Button(
                modifier = Modifier.padding(15.dp),
                onClick = {
                    submitClickedState.value = false
                    chatUserListViewModel.setPollingStop(true)
                    createViewModel.setPollingStop(true)
                    createViewModel.resetState()
                    planeRoundMultiplayer.initRound()
                    loginViewModel.logout()
                }) {
                Text(text = stringResource(R.string.logout))
            }

            if ((autologin || submitClickedState.value) && loginViewModel.getLoading()) {
                Text(text = stringResource(R.string.loader_text))
            } else if ((autologin || submitClickedState.value)) {
                val error = loginViewModel.getError()
                if (error == null) {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.loginsuccess),
                        Toast.LENGTH_LONG
                    ).show()
                    chatUserListViewModel.pollForPlayersList(loginViewModel.getLoggedInToken()!!,
                    loginViewModel.getLoggedInUserId()!!, loginViewModel.getLoggedInUserName()!!)
                    chatUserListViewModel.pollForChatMessages(loginViewModel.getLoggedInToken()!!,
                        loginViewModel.getLoggedInUserId()!!, loginViewModel.getLoggedInUserName()!!)
                    chatUserListViewModel.pollForNewMessagesFlags()

                    navController.navigate(route = PlanesScreens.Chat.name)

                } else {
                    Toast.makeText(
                        LocalContext.current,
                        loginViewModel.getError(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                submitClickedState.value = false
            }
        } else {
            Text(
                text = stringResource(R.string.login),
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.titleMedium
            )

            CommonTextFieldWithViewModel(
                modifier = Modifier.padding(15.dp),
                loginViewModel,
                { login -> login.getUserName() },
                { login, str -> login.setUserName(str) },
                onAction = KeyboardActions {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
                placeholder = stringResource(R.string.username)
            )

            val passwordVisibility = rememberSaveable {
                mutableStateOf(false)
            }

            PasswordInputFieldWithViewModel(
                modifier = Modifier.padding(15.dp),
                loginViewModel,
                { login -> login.getPassword() },
                { login, str -> login.setPassword(str) },
                onAction = KeyboardActions {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
                placeholder = stringResource(R.string.password),
                passwordVisibility = passwordVisibility
            )

            val validationTest = validationUsernamePasswordLogin(loginViewModel.getUserName(),
                loginViewModel.getPassword(), stringResource(R.string.validation_toolong_login_username),
                stringResource(R.string.validation_empty_login_username),
                stringResource(R.string.validation_toolong_login_password),
                stringResource(R.string.validation_empty_login_password))

            Button(
                modifier = Modifier.padding(15.dp),
                onClick = {
                    submitClickedState.value = true
                    loginViewModel.login()
                },
                enabled = validationTest.trim().isEmpty()) {
                Text(text = stringResource(R.string.submit))
            }

            if (!validationTest.trim().isEmpty()) {
                Text(color = Color.Red,
                    text = validationTest)
            }

            if (submitClickedState.value && loginViewModel.getLoading()) {
                Text(text = stringResource(R.string.loader_text))
            } else if (submitClickedState.value) {
                val error = loginViewModel.getError()
                if (error == null) {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.logoutsuccess),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        LocalContext.current,
                        loginViewModel.getError(),
                        Toast.LENGTH_LONG
                    ).show()
                    submitClickedState.value = false
                }
            }
        }
    }
}

public fun validationUsernamePasswordLogin(username: String, password: String,
                                           tooLongLoginError: String, emptyLoginError: String,
                                           tooLongPasswordError: String, emptyPasswordError: String) : String {
    var retString = ""

    if (username.length > 30) {
        retString += " $tooLongLoginError"
    }

    if (username.isEmpty()) {
        retString += " $emptyLoginError"
    }

    if (password.length > 30) {
        retString += " $tooLongPasswordError"
    }

    if (password.isEmpty()) {
        retString += " $emptyPasswordError"
    }

    return retString
}