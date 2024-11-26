package com.planes.android.login

import com.planes.android.chat.IDatabaseService
import com.planes.multiplayer_engine.responses.ChatMessageResponse
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse

class ReceiveChatMessagesServiceGlobal : IReceiveChatMessagesService {

    override fun startPolling() {
        global_Service!!.startPolling()
    }
    override fun stopPolling() {
        global_Service!!.stopPolling()
    }

    fun createService(databaseService: IDatabaseService) {
        if (global_Service != null) return
        global_Service = ReceiveChatMessagesService(databaseService)
    }

    override fun isPolling(): Boolean {
        return global_Service!!.isPolling()
    }

    override fun setChatFragmentUpdateFunction(updateFunction: (List<ChatMessageResponse>)->Unit) {
        global_Service!!.setChatFragmentUpdateFunction(updateFunction)
    }

    override fun deactivateUpdateOfChat() {
        global_Service!!.deactivateUpdateOfChat()
    }

    override fun setConversationFragmentUpdateFunction(updateFunction: (List<ChatMessageResponse>)->Unit) {
        global_Service!!.setConversationFragmentUpdateFunction(updateFunction)
    }

    override fun deactivateUpdateOfConversation() {
        global_Service!!.deactivateUpdateOfConversation()
    }

    companion object {
        private var global_Service: ReceiveChatMessagesService? = null
    }
}