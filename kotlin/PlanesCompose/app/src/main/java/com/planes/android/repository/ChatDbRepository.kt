package com.planes.android.repository

import com.planes.android.data.ChatDao
import com.planes.android.data.ChatMessage
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class ChatDbRepository @Inject constructor (private val chatDao: ChatDao) {

    fun getMessages(username: String, userid: Long): Flow<List<ChatMessage>> =
        chatDao.getMessages(username, userid)
    fun getMessages(username: String, userid: Long, otherUsername: String, otherUserid: Long, recorderName: String, recorderId : Long): Flow<List<ChatMessage>> =
        chatDao.getMessages(username, userid, otherUsername, otherUserid, recorderName, recorderId)
    suspend fun deleteOldMessages(daysBefore: Int) = chatDao.deleteOldMessages(daysBefore)
    suspend fun addChatMessage(senderId: Long, senderName: String, m : String, createdAt : Date, receiverId : Long, receiverName : String, recorderId: Long, recorderName: String) =
        chatDao.addChatMessage(senderId, senderName, m, createdAt, receiverId, receiverName, recorderId, recorderName)
}

