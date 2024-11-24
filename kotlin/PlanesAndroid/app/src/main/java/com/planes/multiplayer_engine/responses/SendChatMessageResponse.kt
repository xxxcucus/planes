package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class SendChatMessageResponse (
    @SerializedName("sent")
    var m_Sent: Boolean
)