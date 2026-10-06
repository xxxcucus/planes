package com.planes.android.network.game.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StartNewRoundResponse (
    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("newRoundCreated")
    var m_NewRoundCreated: Boolean
)
