package com.planes.android.chat

import android.content.Context

class NewMessagesServiceGlobal : INewMessagesService {

    fun createService() {
        if (NewMessagesServiceGlobal.global_Service != null)
            return
        NewMessagesServiceGlobal.global_Service = NewMessagesService()
    }
    override fun setNewMessage(messageIdent: NewMessageIdent, value: Boolean) {
        global_Service!!.setNewMessage(messageIdent, value)
    }

    override fun getNewMessage(messageIdent: NewMessageIdent): Boolean? {
       return global_Service!!.getNewMessage(messageIdent)
    }

    override fun areNewMessagesForPlayer(receiverName: String, receiverId: Long) : Boolean {
        return global_Service!!.areNewMessagesForPlayer(receiverName, receiverId)
    }

    override fun resetFlags(newMessagesFlags: List<NewMessagesFlag>) {
        return global_Service!!.resetFlags(newMessagesFlags)
    }
    override fun getNewMessagesFlags(): HashMap<NewMessageIdent, Boolean> {
        return global_Service!!.getNewMessagesFlags()
    }

    companion object {
        private var global_Service: NewMessagesService? = null
    }
}