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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.planes.android.widgets.CommonTextFieldWithViewModel
import com.planes.android.widgets.PasswordInputFieldWithViewModel

@Composable
fun LoginScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                navController: NavController,
                loginViewModel: LoginViewModel) {

    currentScreenState.value = PlanesScreens.Login.name
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val submitClickedState = rememberSaveable {
        mutableStateOf(false)
    }

    //TODO: validation
    //TODO: reset round when logging in as a different user

    Column(modifier = modifier.fillMaxSize().verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = stringResource(R.string.login),
            modifier = Modifier.padding(15.dp),
            style = MaterialTheme.typography.titleMedium)

        CommonTextFieldWithViewModel(modifier = Modifier.padding(15.dp),
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

        Button(modifier = Modifier.padding(15.dp),
            onClick = {
                submitClickedState.value = true
                loginViewModel.login()
            }) {
            Text(text = stringResource(R.string.submit))
        }

        if (submitClickedState.value && loginViewModel.getLoading()) {
            Text(text = stringResource(R.string.loader_text))
        } else if (submitClickedState.value) {
            val error = loginViewModel.getError()
            if (error == null)
                Toast.makeText(LocalContext.current, stringResource(R.string.loginsuccess), Toast.LENGTH_LONG).show()
            else
                Toast.makeText(LocalContext.current, loginViewModel.getError(), Toast.LENGTH_LONG).show()
            submitClickedState.value = false
        }
    }
}