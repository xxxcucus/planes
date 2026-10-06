package com.planes.android.network.user.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChatMessageResponse (
    @SerializedName("senderId")
    val m_SenderId: String,

    @SerializedName("senderName")
    val m_SenderName: String,

    @SerializedName("receiverId")
    val m_ReceiverId: String,

    @SerializedName("receiverName")
    val m_ReceiverName: String,

    @SerializedName("message")
    val m_Message: String,

    @SerializedName("createdAt")
    val m_CreatedAt: String
)