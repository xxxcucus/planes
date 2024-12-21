package com.planes.android.chat

interface INewMessagesService {
    fun setNewMessage(player: String, value: Boolean)
    fun getNewMessage(player: String): Boolean?
}