package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class AcquireOpponentPositionsResponse (

    @SerializedName("cancelled")
    var m_Cancelled: Boolean,

    @SerializedName("otherExist")
    var m_OtherExist: Boolean,

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
    var m_Plane3Orient: Int
)