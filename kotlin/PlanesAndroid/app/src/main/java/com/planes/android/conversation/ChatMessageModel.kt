package com.planes.android.conversation

import com.planes.android.chat.ChatMessage
import com.planes.utils.DateTimeUtils
import java.time.Instant
import java.util.Date

class ChatMessageModel(message: String, sentByMe: Boolean, sender: String, timestamp: Date) {
    private var m_Message: String
    private var m_SentByMe: Boolean
    private var m_TimeStamp: Date
    private var m_Sender: String

    init {
        m_Message = message
        m_SentByMe = sentByMe
        m_TimeStamp = timestamp
        m_Sender = sender
    }

    constructor (message: ChatMessage, username: String, userid: Long) : this("", false, "", Date.from(
        Instant.now())) {
        m_Message = message.m_Message
        m_TimeStamp = message.m_CreatedAt
        m_SentByMe = message.m_SenderId == userid.toInt()
        m_Sender = message.m_SenderName
    }

    fun getMessage(): String {
        return m_Message
    }

    fun getSender(): String {
        return m_Sender
    }

    fun getTimestampWithUser(): String {
        val user = if (m_SentByMe) "me" else m_Sender
        return "( " + user + ": on " + DateTimeUtils.getStringFromDateLocal(m_TimeStamp) + " )"
    }

    fun isSentByMe(): Boolean {
        return m_SentByMe
    }

}