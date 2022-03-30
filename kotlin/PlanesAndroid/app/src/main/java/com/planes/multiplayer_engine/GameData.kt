package com.planes.multiplayer_engine

/*it could actually be defined outside the MultiplayerRound like in PlaneGraphicsScene
for future use in the single player game*/

class GameData {
    private var m_GameId: Long = 0
    private var m_RoundId: Long = 0
    private var m_UserId: Long = 0
    private var m_OtherUserId: Long = 0
    private var m_GameName: String = ""
    private var m_OtherUsername: String = ""

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


    fun reset() {
            m_GameId = 0;
            m_RoundId = 0;
            m_UserId = 0;
            m_OtherUserId = 0;
            m_GameName = "";
            m_OtherUsername = "";
        }
}