package com.planes.android.network.user.requests

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class PlayersListRequest (
    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String,

    @SerializedName("lastLoginDay")
    var m_LastLoginDay: Int
) : BasisRequest(m_UserName, m_UserId)