package com.planes.android.chat

import android.content.Context
import androidx.room.Room
import com.planes.multiplayer_engine.responses.ChatMessageResponse
import com.planes.utils.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class DatabaseService internal constructor(private val m_Context: Context) : IDatabaseService {

    private val db : PlanesDatabase
    init {
        db = Room.databaseBuilder(
            m_Context,
            PlanesDatabase::class.java, "planes.db"
        ).build()
    }

    override suspend fun addChatMessage(message: ChatMessageResponse, recorderId: Long, recorderName: String) {
        var senderId = message.m_SenderId.toLong()
        var senderName = message.m_SenderName
        var receiverId = message.m_ReceiverId.toLong()
        var receiverName = message.m_ReceiverName
        var m = message.m_Message

        val date = DateTimeUtils.parseDate(message.m_CreatedAt)

        if (date == null)
            return
        var dao = db.chatDao()

        dao.addChatMessage(senderId, senderName, m, date!!, receiverId, receiverName, recorderId, recorderName)
    }

    override suspend fun getMessages(username : String, userid : Long) : List<ChatMessage> {

        var dao = db.chatDao()
        return dao.getMessages(username, userid)
    }

    override suspend fun getMessages(username : String, userid : Long, otherUsername: String, otherUserid: Long, recorderName: String, recorderId: Long) : List<ChatMessage> {

        var dao = db.chatDao()
        return dao.getMessages(username, userid, otherUsername, otherUserid, recorderName, recorderId)
    }

    override suspend fun deleteOldMessages(daysBefore: Int) {
        var dao = db.chatDao()
        return dao.deleteOldMessages(daysBefore)
    }
}