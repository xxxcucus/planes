package com.planes.android.chat

import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse

class ChatEntryModel(player: UserWithLastLoginResponse) {

    private var m_Player: UserWithLastLoginResponse

    init {
        m_Player = player
    }

    fun getPlayerName(): String {
        return m_Player.m_UserName
    }
}