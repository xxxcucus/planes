package com.planes.android.screens.logout

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.login.LoginViewModel
import kotlin.math.log

@Composable
fun LogoutScreen(modifier: Modifier, currentScreenState: MutableState<String>,
                 navController: NavController, loginViewModel: LoginViewModel
) {
    currentScreenState.value = PlanesScreens.Logout.name

    val submitClickedState = rememberSaveable {
        mutableStateOf(false)
    }

    //TODO: reset round when logging out

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (!loginViewModel.isLoggedIn()) {
            Text(text = stringResource(R.string.nouser))
        } else {
            Text(text = stringResource(R.string.userloggedin) + " " + loginViewModel.getLoggedInUserName())
        }

        Button(modifier = Modifier.padding(15.dp),
            enabled = loginViewModel.isLoggedIn(),
            onClick = {
                submitClickedState.value = true
                loginViewModel.logout()
            }) {
            Text(text = stringResource(R.string.logout))
        }

        if (submitClickedState.value && loginViewModel.getLoading()) {
            Text(text = stringResource(R.string.loader_text))
        } else if (submitClickedState.value) {
            val error = loginViewModel.getError()
            if (error == null)
                Toast.makeText(LocalContext.current, stringResource(R.string.logoutsuccess), Toast.LENGTH_LONG).show()
            else
                Toast.makeText(LocalContext.current, loginViewModel.getError(), Toast.LENGTH_LONG).show()
            submitClickedState.value = false
        }
    }
}