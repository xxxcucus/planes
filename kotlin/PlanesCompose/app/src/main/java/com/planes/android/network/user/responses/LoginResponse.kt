package com.planes.android.network.user.responses

import com.google.gson.annotations.SerializedName

open class LoginResponse(
    @SerializedName("id")
    open val m_Id: String,
    @SerializedName("username")
    open val m_Username: String)