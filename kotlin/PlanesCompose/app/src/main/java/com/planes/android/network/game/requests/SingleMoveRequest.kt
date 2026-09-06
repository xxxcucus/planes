package com.planes.android.network.game.requests

import com.google.gson.annotations.SerializedName

data class SingleMoveRequest (

    @SerializedName("moveIndex")
    var m_MoveIndex: Int,

    @SerializedName("moveX")
    var m_MoveX: Int,

    @SerializedName("moveY")
    var m_MoveY: Int
)