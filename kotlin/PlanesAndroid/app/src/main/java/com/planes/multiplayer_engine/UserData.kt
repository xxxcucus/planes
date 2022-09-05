package com.planes.multiplayer_engine

import java.time.LocalDateTime


class UserData {

    private var m_UserName: String = ""
    private var m_UserPassword: String = ""
    private var m_AuthToken: String = ""
    private var m_UserId: Long = 0

    private var m_LastTokenUpdate: LocalDateTime? = null

    var userName: String
        get() = m_UserName
        set(value) { m_UserName = value }

    var password: String
        get() = m_UserPassword
        set(value) { m_UserPassword = value}

    var authToken: String
        get() = m_AuthToken
        set(value)  { m_AuthToken = value }

    var userId: Long
        get() = m_UserId
        set(value) { m_UserId = value }

    var lastTokenUpdate: LocalDateTime?
        get() = m_LastTokenUpdate
        set(value) { m_LastTokenUpdate = value}

    fun reset() {
        m_AuthToken = ""
        m_UserName = ""
        m_UserPassword = ""
        m_UserId = 0
        m_LastTokenUpdate = null
    }
}