package com.planes.android.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.widgets.CommonTextFieldWithLogin
import com.planes.android.widgets.PasswordInputFieldWithLogin

@Composable
fun LoginScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                navController: NavController,
                loginViewModel: LoginViewModel = viewModel<LoginViewModel>()) {

    currentScreenState.value = PlanesScreens.Login.name
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxSize().verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        CommonTextFieldWithLogin(modifier = Modifier.padding(15.dp),
            loginViewModel,
            { login ->  login.getUserName()},
            { login, str -> login.setUserName(str)},
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
        PasswordInputFieldWithLogin(
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
    }
}