package com.planes.multiplayer_engine.requests

import com.google.gson.annotations.SerializedName
import java.util.*

class SendNotSentMovesRequest (

    @SerializedName("gameId")
    var m_GameId: String,

    @SerializedName("roundId")
    var m_RoundId: String,

    @SerializedName("ownUserId")
    var m_OwnUserId: String,

    @SerializedName("opponentUserId")
    var m_OpponentUserId: String,

    @SerializedName("opponentMoveIndex")
    var m_OpponentMoveIndex: Int,

    @SerializedName("listMoves")
    var m_ListMoves: Vector<SingleMoveRequest>,

    @SerializedName("listNotReceivedMoves")
    var m_ListNotReceivedMoves: Vector<Int>
)