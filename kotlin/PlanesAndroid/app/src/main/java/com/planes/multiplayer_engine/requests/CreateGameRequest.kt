package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName

data class CreateGameRequest (
    @SerializedName("gameName")
    var m_GameName: String,

    @SerializedName("userName")
    var m_UserName: String,

    @SerializedName("userId")
    var m_UserId: String,

    @SerializedName("gameId")
    var m_GameId: String)
