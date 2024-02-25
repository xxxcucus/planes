package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName

data class SendWinnerRequest (
    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("winnerId")
    var m_WinnerId: String,

    @SerializedName("draw")
    var m_Draw: Boolean,

    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String
) : BasisRequest(m_UserName, m_UserId)