package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class CancelRoundResponse (
    @SerializedName("roundId")
    var m_RoundId: String
)
