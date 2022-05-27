package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName

data class SendWinnerRequest (
    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("winnerId")
    var m_WinnerId: String,

    @SerializedName("roundId")
    var m_Draw: Boolean

)