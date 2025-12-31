package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName
import com.planes.android.network.user.requests.BasisRequest

data class StartNewRoundRequest (
    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("opponentUserId")
    var m_OpponentId: String,

    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String
): BasisRequest(m_UserName, m_UserId)