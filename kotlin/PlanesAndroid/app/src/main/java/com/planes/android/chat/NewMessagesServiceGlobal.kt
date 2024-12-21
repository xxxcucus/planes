package com.planes.android.chat

import android.content.Context

class NewMessagesServiceGlobal : INewMessagesService {

    fun createService() {
        if (NewMessagesServiceGlobal.global_Service != null)
            return
        NewMessagesServiceGlobal.global_Service = NewMessagesService()
    }
    override fun setNewMessage(player: String, value: Boolean) {
        global_Service!!.setNewMessage(player, value)
    }

    override fun getNewMessage(player: String): Boolean? {
       return global_Service!!.getNewMessage(player)
    }
    companion object {
        private var global_Service: NewMessagesService? = null
    }
}