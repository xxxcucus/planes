package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName

data class StartNewRoundRequest (
    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("ownUserId")
    var m_UserId: String,

    @SerializedName("opponentUserId")
    var m_OpponentId: String
)