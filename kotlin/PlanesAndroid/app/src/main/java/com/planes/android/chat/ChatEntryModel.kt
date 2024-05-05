package com.planes.android.chat

class ChatEntryModel(playerName: String) {

    private var m_PlayerName: String

    init {
        m_PlayerName = playerName
    }

    fun getPlayerName(): String {
        return m_PlayerName
    }
}