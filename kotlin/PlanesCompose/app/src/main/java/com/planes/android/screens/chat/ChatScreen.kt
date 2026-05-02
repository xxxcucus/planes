package com.planes.android.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.about.AboutEntryRow
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.screens.video.VideoViewModel
import com.planes.android.utils.DateTimeUtils

@Composable
fun ChatScreen(modifier: Modifier, currentScreenState: MutableState<String>,
               navController: NavController, loginViewModel: LoginViewModel,
               viewModel: ChatUserListViewModel = hiltViewModel()) {

    currentScreenState.value = stringResource(R.string.chat)

    //TODO: if not logged in show not logged in
    //TODO: viewModel should be parameter for login screen

    if (!loginViewModel.isLoggedIn()) {

    } else {

        viewModel.pollForPlayersList(
            loginViewModel.getLoggedInToken()!!,
            loginViewModel.getLoggedInUserId()!!, loginViewModel.getLoggedInUserName()!!
        )

        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val users = viewModel.getPlayersList().collectAsStateWithLifecycle().value

                //TODO: loader
                if (users != null) {

                    val filteredUsers = users.filter {
                        it.m_UserId != loginViewModel.getLoggedInUserId()
                    }

                    LazyColumn {
                        items(items = filteredUsers) {
                            ChatEntryRow(it)
                        }
                    }
                }
            }
        }
    }
}

