package com.planes.android.network.user.requests

import com.google.gson.annotations.SerializedName

class ReceiveChatMessagesRequest (
    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String
): BasisRequest(m_UserName, m_UserId)