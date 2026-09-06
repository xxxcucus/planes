package com.planes.android.network.game.responses

import com.google.gson.annotations.SerializedName

data class StartNewRoundResponse (
    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("newRoundCreated")
    var m_NewRoundCreated: Boolean
)
