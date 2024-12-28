package com.planes.android.chat

import android.content.Context
import com.planes.android.login.PlayersListService
import com.planes.android.login.PlayersListServiceGlobal
import com.planes.multiplayer_engine.responses.ChatMessageResponse

class DatabaseServiceGlobal : IDatabaseService {

    fun createService(context: Context) {
        if (global_Service != null)
            return
        global_Service = DatabaseService(context)
    }
    override suspend fun addChatMessage(message: ChatMessageResponse, recorderId: Long, recorderName: String) {
        global_Service!!.addChatMessage(message, recorderId, recorderName)
    }

    override suspend fun getMessages(username : String, userid : Long) : List<ChatMessage> {
        return global_Service!!.getMessages(username, userid)
    }

    override suspend fun getMessages(username : String, userid : Long, otherUsername: String, otherUserid: Long, recorderName: String, recorderId: Long) : List<ChatMessage> {
        return  global_Service!!.getMessages(username, userid, otherUsername, otherUserid, recorderName, recorderId)
    }

    override suspend fun deleteOldMessages(daysBefore: Int) {
        global_Service!!.deleteOldMessages(daysBefore)
    }

    override suspend fun getNewMessagesFlags(): List<NewMessagesFlag> {
        return global_Service!!.getNewMessagesFlags()
    }

    override suspend fun updateNewMessagesFlags(senderName: String, senderId: Long, receiverName: String, receiverId: Long, newMessages: Boolean) {
        global_Service!!.updateNewMessagesFlags(senderName, senderId, receiverName, receiverId, newMessages)
    }
    companion object {
        private var global_Service: DatabaseService? = null
    }
}