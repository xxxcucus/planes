package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("id")
    val m_Id: String,
    @SerializedName("username")
    val m_Username: String)