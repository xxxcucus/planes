package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName
import java.util.Vector

data class PlayersListResponse (
    @SerializedName("users")
    val m_Users: Vector<SingleUserResponse>
)
