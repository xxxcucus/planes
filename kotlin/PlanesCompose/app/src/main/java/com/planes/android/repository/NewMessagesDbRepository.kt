package com.planes.android.repository

import com.planes.android.data.ChatDao
import com.planes.android.data.NewMessagesDao
import com.planes.android.data.NewMessagesFlag
import javax.inject.Inject

class NewMessagesDbRepository @Inject constructor (private val newMessagesDao: NewMessagesDao) {

    suspend fun updateNewMessagesFlags(senderName: String, senderId: Long, receiverName: String, receiverId: Long, newMessages: Boolean) {
        val flags = newMessagesDao.findNewMessage(senderName, senderId, receiverName, receiverId)

        if (flags.isEmpty())
            newMessagesDao.insertNewMessage(senderName, senderId, receiverName, receiverId, newMessages)
        else
            newMessagesDao.updateNewMessage(senderName, senderId, receiverName, receiverId, newMessages)
    }

    fun getNewMessagesFlags() = newMessagesDao.getNewMessagesFlags()

}


