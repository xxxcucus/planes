package com.planes.android.network.game.responses

import com.google.gson.annotations.SerializedName
import com.planes.android.network.game.requests.SingleMoveRequest
import java.util.*

class SendNotSentMovesResponse (

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("opponentUserId")
    var m_OpponentUserId: String,

    @SerializedName("startIndex")
    var m_StartIndex: Int,

    @SerializedName("cancelled")
    var m_Cancelled: Boolean,

    @SerializedName("listMoves")
    var m_ListMoves: Vector<SingleMoveRequest>
)