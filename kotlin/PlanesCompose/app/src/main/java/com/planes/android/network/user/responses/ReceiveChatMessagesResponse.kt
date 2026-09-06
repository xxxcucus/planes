package com.planes.android.network.user.responses

import com.google.gson.annotations.SerializedName

data class ReceiveChatMessagesResponse (
    @SerializedName("messages")
    val m_Messages: List<ChatMessageResponse>
)

