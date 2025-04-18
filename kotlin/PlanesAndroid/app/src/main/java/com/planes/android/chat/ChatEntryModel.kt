package com.planes.android.chat

import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import com.planes.utils.DateTimeUtils
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


class ChatEntryModel(player: UserWithLastLoginResponse) {

    private var m_Player: UserWithLastLoginResponse
    private var m_NewMessages = false

    init {
        m_Player = player
    }

    fun getPlayerName(): String {
        return m_Player.m_UserName
    }

    fun getPlayerId(): Long {
        return m_Player.m_UserId.toLong()
    }

    fun setNewMessages(flag: Boolean) {
        m_NewMessages = flag;
    }

    fun areNewMessages() : Boolean {
        return m_NewMessages
    }

    fun isPlayerOnline(): Boolean {
        val date = DateTimeUtils.parseDate(m_Player.m_LastLogin)
        if (date == null)
            return false
        return getDateDiff(date, Date.from(Instant.now()), TimeUnit.MINUTES) < 30;
    }
    fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        val diffInMillies: Long = date2.getTime() - date1.getTime()
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }
}