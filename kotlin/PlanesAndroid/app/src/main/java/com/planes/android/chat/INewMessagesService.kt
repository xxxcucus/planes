package com.planes.android.chat

interface INewMessagesService {
    fun setNewMessage(messageIdent: NewMessageIdent, value: Boolean)
    fun getNewMessage(messageIdent: NewMessageIdent): Boolean?
}