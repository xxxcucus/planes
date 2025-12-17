package com.planes.android.screens.register

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.widgets.CommonTextFieldWithLogin
import com.planes.android.widgets.CommonTextFieldWithRegister
import com.planes.android.widgets.PasswordInputFieldWithLogin
import com.planes.android.widgets.PasswordInputFieldWithRegister

@Composable
fun RegisterScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                   navController: NavController,
                   registerViewModel: RegisterViewModel = hiltViewModel())  {

    currentScreenState.value = PlanesScreens.Register.name
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.fillMaxSize().verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.register),
            modifier = Modifier.padding(15.dp),
            style = MaterialTheme.typography.titleMedium)

        CommonTextFieldWithRegister(modifier = Modifier.padding(15.dp),
            registerViewModel,
            { register ->  register.getUserName()},
            { register, str -> register.setUserName(str)},
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

        PasswordInputFieldWithRegister(
            modifier = Modifier.padding(15.dp),
            registerViewModel,
            { register -> register.getPassword() },
            { register, str -> register.setPassword(str) },
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

            }) {
            Text(text = stringResource(R.string.submit))
        }
    }
}