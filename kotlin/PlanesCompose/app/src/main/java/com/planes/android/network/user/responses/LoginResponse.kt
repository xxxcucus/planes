package com.planes.android.network.user.responses

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("id")
    val m_Id: String,
    @SerializedName("username")
    val m_Username: String)