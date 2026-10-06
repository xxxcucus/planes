package com.planes.android.network.game.requests

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.planes.android.network.user.requests.BasisRequest

@Keep
data class GameStatusRequest (
    @SerializedName("gameName")
    var m_GameName: String,

    @SerializedName("userName")
    override var m_UserName: String,

    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("gameId")
    var m_GameId: String): BasisRequest(m_UserName, m_UserId)
