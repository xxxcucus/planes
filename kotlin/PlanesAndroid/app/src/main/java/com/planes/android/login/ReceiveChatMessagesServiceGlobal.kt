package com.planes.android.login

import com.planes.android.chat.IDatabaseService
import com.planes.android.chat.INewMessagesService
import com.planes.multiplayer_engine.responses.ChatMessageResponse
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse

class ReceiveChatMessagesServiceGlobal : IReceiveChatMessagesService {

    override fun startPolling() {
        global_Service!!.startPolling()
    }
    override fun stopPolling() {
        global_Service!!.stopPolling()
    }

    fun createService(databaseService: IDatabaseService, newMessagesService: INewMessagesService) {
        if (global_Service != null) return
        global_Service = ReceiveChatMessagesService(databaseService, newMessagesService)
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

    override fun setMainActivityUpdateFunction(updateFunction: ()->Unit) {
        global_Service!!.setMainActivityUpdateFunction(updateFunction)
    }

    override fun deactivateUpdateOfMainActivity() {
        global_Service!!.deactivateUpdateOfMainActivity()
    }

    companion object {
        private var global_Service: ReceiveChatMessagesService? = null
    }
}