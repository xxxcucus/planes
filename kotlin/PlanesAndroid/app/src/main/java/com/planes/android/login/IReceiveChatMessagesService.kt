package com.planes.android.login

import com.planes.multiplayer_engine.responses.ChatMessageResponse

interface IReceiveChatMessagesService {
    fun startPolling()
    fun stopPolling()

    fun isPolling(): Boolean

    fun setChatFragmentUpdateFunction(updateFunction: (List<ChatMessageResponse>)->Unit)

    fun deactivateUpdateOfChat()

    fun setConversationFragmentUpdateFunction(updateFunction: (List<ChatMessageResponse>)->Unit)

    fun deactivateUpdateOfConversation()

    fun setMainActivityUpdateFunction(updateFunction: ()->Unit)

    fun deactivateUpdateOfMainActivity()
}