package com.planes.android.network.user.requests

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class ReceiveChatMessagesRequest (
    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String
): BasisRequest(m_UserName, m_UserId)