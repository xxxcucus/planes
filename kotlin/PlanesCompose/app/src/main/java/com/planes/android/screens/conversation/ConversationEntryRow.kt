package com.planes.android.screens.conversation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.planes.android.data.ChatMessage

@Composable
fun ConversationEntryRow(message: ChatMessage) {
    Text(text = message.m_Message)
}