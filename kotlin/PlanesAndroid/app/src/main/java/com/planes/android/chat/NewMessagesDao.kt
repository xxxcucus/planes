package com.planes.android.chat

import androidx.room.Dao
import androidx.room.Query

@Dao
interface NewMessagesDao {

    @Query("UPDATE NewMessages  SET new_messages = :newMessages where sender_id = :senderId and sender_name = :senderName and receiver_id = :receiverId and receiver_name = :receiverName")
    suspend fun updateNewMessage(senderName: String, senderId: Long, receiverName: String, receiverId: Long, newMessages: Boolean)

    @Query("SELECT id, new_messages, sender_id, sender_name, receiver_name, receiver_id FROM NewMessages WHERE sender_id = :senderId and sender_name = :senderName and receiver_id = :receiverId and receiver_name = :receiverName")
    suspend fun findNewMessage(senderName: String, senderId: Long, receiverName: String, receiverId: Long) : List<NewMessagesFlag>

    @Query("INSERT INTO NewMessages (sender_id, sender_name, receiver_id, receiver_name, new_messages) VALUES (:senderId, :senderName, :receiverId, :receiverName, :newMessages)")
    suspend fun insertNewMessage(senderName: String, senderId: Long, receiverName: String, receiverId: Long, newMessages: Boolean)

    @Query("SELECT id, new_messages, sender_id, sender_name, receiver_name, receiver_id FROM NewMessages")
    suspend fun getNewMessagesFlags() : List<NewMessagesFlag>

}
