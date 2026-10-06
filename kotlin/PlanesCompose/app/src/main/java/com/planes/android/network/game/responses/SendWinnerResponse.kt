package com.planes.android.network.game.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SendWinnerResponse (

    @SerializedName("roundId")
    var m_RoundId: String
)