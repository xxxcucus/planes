package com.planes.android.screens.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens
import com.planes.android.widgets.ChatMessageInputFieldWithViewModel

@Composable
fun ConversationScreen(modifier: Modifier,
                                    currentScreenState: MutableState<String>,
                                    navController: NavController,
                       conversationViewModel: ConversationViewModel = hiltViewModel()
) {

    //TODO: conversation with
    currentScreenState.value = PlanesScreens.Conversation.name

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        val keyboardController = LocalSoftwareKeyboardController.current

        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {

        }

        ChatMessageInputFieldWithViewModel(modifier = Modifier.padding(15.dp).fillMaxWidth(),
            conversationViewModel,
            { conversation -> conversation.getTextToSend() },
            { conversation, str -> conversation.setTextToSend(str) },
            onAction = KeyboardActions {
                keyboardController?.hide()
            },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default,
            placeholder = stringResource(R.string.inputmessage))

        Button(
            modifier = Modifier.padding(15.dp).fillMaxWidth(),
            onClick = {
            }) {
            Text(text = stringResource(R.string.sendmessage))
        }
    }
}