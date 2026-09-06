package com.planes.android.network.user.responses

import com.google.gson.annotations.SerializedName

data class UserWithLastLoginResponse (
    @SerializedName("username")
    val m_UserName : String,

    @SerializedName("userid")
    val m_UserId: String,

    @SerializedName("lastLogin")
    val m_LastLogin: String
)