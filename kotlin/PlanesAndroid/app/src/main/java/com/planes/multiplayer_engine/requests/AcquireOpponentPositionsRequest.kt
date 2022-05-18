package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName

data class AcquireOpponentPositionsRequest (

    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("ownUserId")
    var m_OwnUserId: String,

    @SerializedName("opponentUserId")
    var m_OpponentUserId: String
)