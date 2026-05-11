package com.planes.android.screens.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.screens.login.LoginViewModel
import com.planes.android.widgets.ChatMessageInputFieldWithViewModel

@Composable
fun ConversationScreen(modifier: Modifier,
                                    currentScreenState: MutableState<String>,
                                    navController: NavController,
                       chatPartnerId: String, chatPartnerUsername: String,
                       loginViewModel: LoginViewModel,
                       conversationViewModel: ConversationViewModel = hiltViewModel()
) {

    //TODO: conversation with
    currentScreenState.value = "${PlanesScreens.Conversation.name} with $chatPartnerUsername "

    //TODO: if not logged in

    if (loginViewModel.isLoggedIn()) {

        conversationViewModel.setCredentials(loginViewModel.getLoggedInTokenState(),
            loginViewModel.getLoggedInUsernameState(), loginViewModel.getLoggedInUserIdState(),
            chatPartnerId, chatPartnerUsername)

        conversationViewModel.updateChatMessagesFromDb()

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val keyboardController = LocalSoftwareKeyboardController.current

            val messages = conversationViewModel.getMessagesList().collectAsStateWithLifecycle().value

            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(items = messages) {
                    ConversationEntryRow(it)
                }
            }

            ChatMessageInputFieldWithViewModel(
                modifier = Modifier.padding(15.dp).fillMaxWidth(),
                conversationViewModel,
                { conversation -> conversation.getTextToSend() },
                { conversation, str -> conversation.setTextToSend(str) },
                onAction = KeyboardActions {
                    keyboardController?.hide()
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
                placeholder = stringResource(R.string.inputmessage)
            )

            Button(
                modifier = Modifier.padding(15.dp).fillMaxWidth(),
                onClick = {
                    conversationViewModel.sendMessage()
                    conversationViewModel.resetMessage()
                    conversationViewModel.updateChatMessagesFromDb()
                }) {
                Text(text = stringResource(R.string.sendmessage))
            }
        }
    }
}