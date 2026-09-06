package com.planes.android.network.user.responses

import com.google.gson.annotations.SerializedName

data class PlayersListResponse (
    @SerializedName("users")
    val m_Usernames: List<UserWithLastLoginResponse>
)
