package com.planes.android.network.game.responses

import com.google.gson.annotations.SerializedName

data class CancelRoundResponse (
    @SerializedName("roundId")
    var m_RoundId: String
)
