package com.planes.android.network.user.requests

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginRequest(
    @SerializedName("username")
    var m_Username: String,

    @SerializedName("password")
    var m_Password: String)