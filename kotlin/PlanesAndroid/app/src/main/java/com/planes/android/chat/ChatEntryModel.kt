package com.planes.android.chat

import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


class ChatEntryModel(player: UserWithLastLoginResponse) {

    private var m_Player: UserWithLastLoginResponse

    init {
        m_Player = player
    }

    fun getPlayerName(): String {
        return m_Player.m_UserName
    }

    fun isPlayerOnline(): Boolean {
        val formatter = SimpleDateFormat("dd MM yyyy HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("UTC");
        var formattedDate = m_Player.m_LastLogin
        val date: Date = formatter.parse(formattedDate)

        return getDateDiff(date, Date.from(Instant.now()), TimeUnit.MINUTES) < 30;
    }
    fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        val diffInMillies: Long = date2.getTime() - date1.getTime()
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }
}