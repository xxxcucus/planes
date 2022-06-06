package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName

data class CancelRoundRequest (
    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String
)