package com.planes.android.chat

import android.content.Context
import androidx.room.Room
import com.planes.multiplayer_engine.responses.ChatMessageResponse
import com.planes.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class DatabaseService internal constructor(private val m_Context: Context) : IDatabaseService {

    private val db : PlanesDatabase = Room.databaseBuilder(
        m_Context,
        PlanesDatabase::class.java, "planes.db"
    ).build()

    private val disp = Dispatchers.IO.limitedParallelism(10)
    private val scope = CoroutineScope(SupervisorJob() + disp)

    override fun addChatMessage(message: ChatMessageResponse, recorderId: Long, recorderName: String) {
        var senderId = message.m_SenderId.toLong()
        var senderName = message.m_SenderName
        var receiverId = message.m_ReceiverId.toLong()
        var receiverName = message.m_ReceiverName
        var m = message.m_Message

        val date = DateTimeUtils.parseDate(message.m_CreatedAt)

        if (date == null)
            return
        val dao = db.chatDao()

        scope.launch {
            dao.addChatMessage(
                senderId,
                senderName,
                m,
                date!!,
                receiverId,
                receiverName,
                recorderId,
                recorderName
            )
        }
    }

    override suspend fun getMessages(username : String, userid : Long) : List<ChatMessage> {

        val dao = db.chatDao()
        return dao.getMessages(username, userid)
    }

    override suspend fun getMessages(username : String, userid : Long, otherUsername: String, otherUserid: Long, recorderName: String, recorderId: Long) : List<ChatMessage> {

        val dao = db.chatDao()
        return dao.getMessages(username, userid, otherUsername, otherUserid, recorderName, recorderId)
    }

    override fun deleteOldMessages(daysBefore: Int) {
        val dao = db.chatDao()
        scope.launch {
            dao.deleteOldMessages(daysBefore)
        }
    }

    override suspend fun getNewMessagesFlags(): List<NewMessagesFlag> {
        val dao = db.newMessagesDao()
        return dao.getNewMessagesFlags()
    }

    override fun updateNewMessagesFlags(senderName: String, senderId: Long, receiverName: String, receiverId: Long, newMessages: Boolean) {
        val dao = db.newMessagesDao()

        scope.launch {
            val flags = dao.findNewMessage(senderName, senderId, receiverName, receiverId)

            if (flags.isEmpty())
                dao.insertNewMessage(senderName, senderId, receiverName, receiverId, newMessages)
            else
                dao.updateNewMessage(senderName, senderId, receiverName, receiverId, newMessages)
        }
    }
}