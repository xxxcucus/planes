package com.planes.android.network.game.requests

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.planes.android.network.user.requests.BasisRequest
import java.util.*

@Keep
class SendNotSentMovesRequest (

    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("opponentUserId")
    var m_OpponentUserId: String,

    @SerializedName("opponentMoveIndex")
    var m_OpponentMoveIndex: Int,

    @SerializedName("listMoves")
    var m_ListMoves: Vector<SingleMoveRequest>,

    @SerializedName("listNotReceivedMoves")
    var m_ListNotReceivedMoves: Vector<Int>,

    @SerializedName("userId")
    override var m_UserId: String,

    @SerializedName("userName")
    override var m_UserName: String
) : BasisRequest(m_UserName, m_UserId)