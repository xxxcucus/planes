package com.planes.android.chat

import android.content.Context
import androidx.room.Room
import com.planes.multiplayer_engine.responses.ChatMessageResponse
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

    override fun addChatMessage(message: ChatMessageResponse, recorderId: Long, recorderName: String) {
        var senderId = message.m_SenderId.toLong()
        var senderName = message.m_SenderName
        var receiverId = message.m_ReceiverId.toLong()
        var receiverName = message.m_ReceiverName
        var m = message.m_Message

        val formatter = SimpleDateFormat("dd MM yyyy HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("UTC");
        var formattedDate = message.m_CreatedAt
        val date: Date = formatter.parse(formattedDate)

        var dao = db.chatDao()

        dao.addChatMessage(senderId, senderName, m, date, receiverId, receiverName, recorderId, recorderName)
    }

}