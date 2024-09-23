package com.planes.android.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface ChatDao {

    @Query("INSERT into ChatMessages (sender_id, sender_name, message, created_at, receiver_id, receiver_name, recorder_id, recorder_name) VALUES (:senderId, :senderName, :m, :createdAt, :receiverId, :receiverName, :recorderId, :recorderName)")
    suspend fun addChatMessage(senderId: Long, senderName: String, m : String, createdAt : Date, receiverId : Long, receiverName : String, recorderId: Long, recorderName: String)

    @Query("DELETE from ChatMessages WHERE current_timestamp - created_at <= :daysBefore * 24 * 60 * 60 * 1000 ")
    suspend fun deleteOldMessages(daysBefore: Int)

    @Query("SELECT id, sender_id, sender_name, message, created_at, receiver_id, receiver_name, recorder_id, recorder_name FROM ChatMessages WHERE ((receiver_id = :userid and receiver_name = :username) or (sender_id = :userid and sender_name = :username)) and recorder_id = :userid and recorder_name = :username ORDER BY created_at ASC")
    suspend fun getMessages(username: String, userid: Long) : List<ChatMessage>
}