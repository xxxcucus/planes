package com.planes.android.chat

import android.content.Context
import com.planes.android.login.PlayersListService
import com.planes.android.login.PlayersListServiceGlobal
import com.planes.multiplayer_engine.responses.ChatMessageResponse

class DatabaseServiceGlobal : IDatabaseService {

    fun createService(context: Context) {
        if (global_Service != null)
            return
        global_Service = DatabaseService(context)
    }
    override fun addChatMessage(message: ChatMessageResponse, recorderId: Long, recorderName: String) {
        global_Service!!.addChatMessage(message, recorderId, recorderName)
    }
    companion object {
        private var global_Service: DatabaseService? = null
    }
}