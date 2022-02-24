package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("timestamp")
    var m_TimeStamp: String,
    @SerializedName("status")
    var m_Status: Int,
    @SerializedName("error")
    var m_Error: String,
    @SerializedName("message")
    var m_Message: String,
    @SerializedName("path")
    var m_Path: String
)