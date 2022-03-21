package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName

data class NoRobotRequest(
    @SerializedName("id")
    var m_RequestId: String,

    @SerializedName("answer")
    var m_Answer: String)