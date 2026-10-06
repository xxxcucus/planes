package com.planes.android.network.user.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReceiveChatMessagesResponse (
    @SerializedName("messages")
    val m_Messages: List<ChatMessageResponse>
)

