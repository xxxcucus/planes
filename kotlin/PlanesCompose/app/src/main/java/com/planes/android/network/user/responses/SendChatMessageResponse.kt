package com.planes.android.network.user.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SendChatMessageResponse (
    @SerializedName("sent")
    var m_Sent: Boolean,

    @SerializedName("messageId")
    var m_MessageId: String
)