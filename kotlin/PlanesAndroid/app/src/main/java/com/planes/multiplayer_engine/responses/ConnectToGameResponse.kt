package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class ConnectToGameResponse (
    @SerializedName("exists")
    var m_Exists: Boolean,

    @SerializedName("id")
    var m_GameId: String,

    @SerializedName("gameName")
    var m_GameName: String,

    @SerializedName("firstPlayerName")
    var m_FirstPlayerName: String,

    @SerializedName("secondPlayerName")
    var m_SecondPlayerName: String,

    @SerializedName("firstPlayerId")
    var m_FirstPlayerId: String,

    @SerializedName("secondPlayerId")
    var m_SecondPlayerId: String,

    @SerializedName("currentRoundId")
    var m_CurrentRoundId: String

)