package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName
import com.planes.android.network.user.requests.BasisRequest

data class AcquireOpponentPositionsRequest (

    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("opponentUserId")
    var m_OpponentUserId: String,

    @SerializedName("userName")
    override var m_UserName: String,

    @SerializedName("userId")
    override var m_UserId: String
    ): BasisRequest(m_UserName, m_UserId)