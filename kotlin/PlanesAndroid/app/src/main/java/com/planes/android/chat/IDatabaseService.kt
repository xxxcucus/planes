package com.planes.android.chat

import com.planes.multiplayer_engine.responses.ChatMessageResponse

interface IDatabaseService {
    suspend fun addChatMessage(message: ChatMessageResponse, recorderId: Long, recorderName: String)
    suspend fun getMessages(username : String, userid : Long) : List<ChatMessage>
    suspend fun getMessages(username : String, userid : Long, otherUsername: String, otherUserid: Long, recorderName: String, recorderId: Long) : List<ChatMessage>
}
