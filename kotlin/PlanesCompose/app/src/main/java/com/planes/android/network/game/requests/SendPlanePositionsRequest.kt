package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName
import com.planes.android.network.user.requests.BasisRequest

data class SendPlanePositionsRequest(

    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("opponentUserId")
    var m_OpponentUserId: String,

    @SerializedName("plane1_x")
    var m_Plane1X: Int,

    @SerializedName("plane1_y")
    var m_Plane1Y: Int,

    @SerializedName("plane1_orient")
    var m_Plane1Orient: Int,

    @SerializedName("plane2_x")
    var m_Plane2X: Int,

    @SerializedName("plane2_y")
    var m_Plane2Y: Int,

    @SerializedName("plane2_orient")
    var m_Plane2Orient: Int,

    @SerializedName("plane3_x")
    var m_Plane3X: Int,

    @SerializedName("plane3_y")
    var m_Plane3Y: Int,

    @SerializedName("plane3_orient")
    var m_Plane3Orient: Int,

    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String
    ): BasisRequest(m_UserName, m_UserId)
