package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName
import java.util.Vector

data class PlayersListResponse (
    @SerializedName("usernames")
    val m_Usernames: List<UserWithLastLoginResponse>
)
