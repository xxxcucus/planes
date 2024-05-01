package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class SingleUserResponse (
    @SerializedName("id")
    val m_Id: String,

    @SerializedName("username")
    val m_Username: String,

    @SerializedName("status")
    val m_Status: String,

    @SerializedName("createdAt")
    val createdAt: String
)