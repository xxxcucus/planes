package com.planes.android.chat

interface INewMessagesService {
    fun setNewMessage(messageIdent: NewMessageIdent, value: Boolean)
    fun getNewMessage(messageIdent: NewMessageIdent): Boolean?

    fun areNewMessagesForPlayer(receiverName: String, receiverId: Long) : Boolean
}