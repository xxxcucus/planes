package com.planes.android.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatDao {

    @Insert
    fun addChatMessage(message: ChatMessage)

    @Query("DELETE from ChatMessages WHERE created_at < DATEADD('day', -:daysBefore, GETDATE()) ")
    fun deleteOldMessages(daysBefore: Int)

    @Query("SELECT sender_id, sender_name, message, created_at, receiver_id, receiver_name FROM ChatMessages WHERE ((receiver_id = :userid and receiver_name = :username) or (sender_id = :userid and sender_name = :username)) and recorder_id = :userid and recorder_name = :username ORDER BY created_at ASC")
    fun getMessages(username: String, userid: Long)
}