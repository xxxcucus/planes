package com.planes.multiplayer_engine

class UserData {

    private var m_UserName: String = ""
    private var m_UserPassword: String = ""
    private var m_AuthToken: String = ""
    private var m_UserId: Long = 0

    var userName: String
        get() = m_UserName
        set(value) { m_UserName = userName }

    var password: String
        get() = m_UserPassword
        set(value) { m_UserPassword = value}

    var authToken: String
        get() = m_AuthToken
        set(value)  { m_AuthToken = value }

    var userId: Long
        get() = m_UserId
        set(value) { m_UserId = value }


    fun reset() {
        m_AuthToken = ""
        m_UserName = ""
        m_UserPassword = ""
        m_UserId = 0
    }
}