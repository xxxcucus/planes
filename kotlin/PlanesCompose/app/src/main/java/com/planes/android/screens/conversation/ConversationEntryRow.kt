package com.planes.android.screens.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.planes.android.data.ChatMessage
import com.planes.android.utils.DateTimeUtils

@Composable
fun ConversationEntryRow(message: ChatMessage, isItMe: Boolean, otherName: String) {

    if (isItMe) {
        Column() {
            Text(
                modifier = Modifier.padding(all = 5.dp),
                textAlign = TextAlign.Start,
                text = message.m_Message
            )
            Text (
                modifier = Modifier.padding(all = 5.dp),
                textAlign = TextAlign.Start,
                text = "(me : on  ${DateTimeUtils.getStringFromDateLocal(message.m_CreatedAt)})"
            )
        }
    } else {
        Column() {
            Text(
                modifier = Modifier.padding(all = 5.dp),
                textAlign = TextAlign.End,
                text = message.m_Message
            )
            Text(
                modifier = Modifier.padding(all = 5.dp),
                textAlign = TextAlign.End,
                text = "($otherName : on  ${DateTimeUtils.getStringFromDateLocal(message.m_CreatedAt)})"
            )
        }
    }
}