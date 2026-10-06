package com.planes.android.network.user.requests

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NoRobotRequest(
    @SerializedName("id")
    var m_RequestId: String,

    @SerializedName("answer")
    var m_Answer: String)