package com.planes.android.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.login.LoginViewModel

@Composable
fun ChatScreen(modifier: Modifier, currentTitleState: MutableState<String>,
               currentScreenState: MutableState<String>,
               showPopupState: MutableState<Boolean>,
               navController: NavController, loginViewModel: LoginViewModel,
               viewModel: ChatUserListViewModel) {

    currentTitleState.value = stringResource(R.string.chat)
    currentScreenState.value = PlanesScreens.Chat.name
    showPopupState.value = false

    //TODO: if not logged in show not logged in
    //TODO: viewModel should be parameter for login screen



    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!loginViewModel.isLoggedIn()) {
                Text(text = stringResource(R.string.nouser))
            } else {

                val users = viewModel.getPlayersList().collectAsStateWithLifecycle().value
                val messagesFlags =
                    viewModel.getNewMessagesFlags().collectAsStateWithLifecycle().value

                //TODO: loader
                if (users != null) {

                    val filteredUsers = users.filter {
                        it.m_UserId != loginViewModel.getLoggedInUserId()
                    }.map { user ->
                        UserWithLastLoginAndNewMessagesFlag(
                            user.m_UserName, user.m_UserId, user.m_LastLogin,
                            messagesFlags.firstOrNull {
                                it.m_SenderId == user.m_UserId.toInt() &&
                                        it.m_ReceiverId == loginViewModel.getLoggedInUserId()
                                    ?.toInt()!!
                            }?.m_NewMessages == true
                        )
                    }

                    //TODO: enrich with new messages flag

                    LazyColumn {
                        items(items = filteredUsers) {
                            ChatEntryRow(
                                it, loginViewModel.getLoggedInUserId()?.toLong()!!,
                                loginViewModel.getLoggedInUserName()!!, navController, viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

