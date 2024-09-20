package com.planes.android.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface ChatDao {

    @Query("INSERT into ChatMessages (sender_id, sender_name, message, created_at, receiver_id, receiver_name, recorder_id, recorder_name) VALUES (:senderId, :senderName, :m, :createdAt, :receiverId, :receiverName, :recorderId, :recorderName)")
    fun addChatMessage(senderId: Long, senderName: String, m : String, createdAt : Date, receiverId : Long, receiverName : String, recorderId: Long, recorderName: String)

    @Query("DELETE from ChatMessages WHERE created_at < DATEADD('day', -:daysBefore, GETDATE()) ")
    fun deleteOldMessages(daysBefore: Int)

    @Query("SELECT sender_id, sender_name, message, created_at, receiver_id, receiver_name FROM ChatMessages WHERE ((receiver_id = :userid and receiver_name = :username) or (sender_id = :userid and sender_name = :username)) and recorder_id = :userid and recorder_name = :username ORDER BY created_at ASC")
    fun getMessages(username: String, userid: Long)
}