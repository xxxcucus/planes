package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class NoRobotResponse (
    @SerializedName("id")
    var m_UserId: String,

    @SerializedName("username")
    var m_Username: String,

    @SerializedName("status")
    var m_Status: String,

    @SerializedName("createdAt")
    var m_CreatedAt: String,
)