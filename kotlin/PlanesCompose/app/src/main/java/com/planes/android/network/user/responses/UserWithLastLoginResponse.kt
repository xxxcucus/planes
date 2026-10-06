package com.planes.android.network.user.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserWithLastLoginResponse (
    @SerializedName("username")
    val m_UserName : String,

    @SerializedName("userid")
    val m_UserId: String,

    @SerializedName("lastLogin")
    val m_LastLogin: String
)