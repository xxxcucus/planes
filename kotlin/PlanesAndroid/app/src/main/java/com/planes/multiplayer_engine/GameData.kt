package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.responses.ConnectToGameResponse
import com.planes.multiplayer_engine.responses.CreateGameResponse
import com.planes.multiplayer_engine.responses.GameStatusResponse

/*it could actually be defined outside the MultiplayerRound like in PlaneGraphicsScene
for future use in the single player game*/

class GameData {
    private var m_GameId: Long = 0
    private var m_RoundId: Long = 0
    private var m_UserId: Long = 0
    private var m_OtherUserId: Long = 0
    private var m_GameName: String = ""
    private var m_OtherUsername: String = ""
    private var m_UserName: String = ""

    //TODO: set to zero when logging in
    var gameId: Long
        get() = m_GameId
        set(value) { m_GameId = value }

    var roundId: Long
        get() = m_RoundId
        set(value) { m_RoundId = value }

    var userId: Long
        get() = m_UserId
        set(value) { m_UserId = value }

    var otherUserId: Long
        get() = m_OtherUserId
        set(value) { m_OtherUserId = value }

    var gameName: String
        get() = m_GameName
        set(value) { m_GameName = value}

    var otherUsername: String
        get() = m_OtherUsername
        set(value) { m_OtherUsername = value }

    var username: String
        get() = m_UserName
        set(value) { m_UserName = value }

    fun reset() {
            m_GameId = 0;
            m_RoundId = 0;
            m_UserId = 0;
            m_OtherUserId = 0;
            m_GameName = "";
            m_OtherUsername = "";
        }

    fun setFromCreateGameResponse(gameCreationResponse: CreateGameResponse) {
        m_GameId = gameCreationResponse.m_GameId.toLong()
        m_RoundId = gameCreationResponse.m_CurrentRoundId.toLong()
        m_UserId = gameCreationResponse.m_SecondPlayerId.toLong()
        m_OtherUserId = gameCreationResponse.m_FirstPlayerId.toLong()
        m_GameName = gameCreationResponse.m_GameName
        m_OtherUsername = gameCreationResponse.m_FirstPlayerName
        m_UserName = gameCreationResponse.m_SecondPlayerName
    }

    fun setFromConnectToGameResponse(gameCreationResponse: ConnectToGameResponse) {
        m_GameId = gameCreationResponse.m_GameId.toLong()
        m_RoundId = gameCreationResponse.m_CurrentRoundId.toLong()
        m_UserId = gameCreationResponse.m_SecondPlayerId.toLong()
        m_OtherUserId = gameCreationResponse.m_FirstPlayerId.toLong()
        m_GameName = gameCreationResponse.m_GameName
        m_OtherUsername = gameCreationResponse.m_FirstPlayerName
        m_UserName = gameCreationResponse.m_SecondPlayerName
    }

    fun setFromGameStatusResponse(gameStatusResponse: GameStatusResponse, userId: Long, userName: String) {
        m_GameId = gameStatusResponse.m_GameId.toLong()
        m_RoundId = gameStatusResponse.m_CurrentRoundId.toLong()
        m_UserId = userId
        m_UserName = userName
        m_OtherUserId = if (gameStatusResponse.m_FirstPlayerId.toLong() == userId) gameStatusResponse.m_SecondPlayerId.toLong() else gameStatusResponse.m_FirstPlayerId.toLong()
        m_GameName = gameStatusResponse.m_GameName
        m_OtherUsername = if (gameStatusResponse.m_FirstPlayerId.toLong() == userId) gameStatusResponse.m_SecondPlayerName else gameStatusResponse.m_FirstPlayerName
    }
}