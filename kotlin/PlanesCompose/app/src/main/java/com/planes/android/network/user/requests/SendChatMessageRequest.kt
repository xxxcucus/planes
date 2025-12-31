package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName
import com.planes.android.network.user.requests.BasisRequest

class SendChatMessageRequest (
    @SerializedName("receiverId")
    val m_ReceiverId: String,

    @SerializedName("message")
    val m_Message: String,

    @SerializedName("messageId")
    val m_MessageId: String,

    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String
) : BasisRequest(m_UserName, m_UserId)