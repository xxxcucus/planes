package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class ReceiveChatMessagesResponse (
    @SerializedName("messages")
    val m_Messages: List<ChatMessageResponse>
)

