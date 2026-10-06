package com.planes.android.network.user.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PlayersListResponse (
    @SerializedName("users")
    val m_Usernames: List<UserWithLastLoginResponse>
)
