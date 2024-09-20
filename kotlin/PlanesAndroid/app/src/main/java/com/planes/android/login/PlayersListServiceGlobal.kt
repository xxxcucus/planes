package com.planes.android.login

import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse

class PlayersListServiceGlobal : IPlayersListService {

    override fun startPolling() {
        global_Service!!.startPolling()
    }
    override fun stopPolling() {
        global_Service!!.stopPolling()
    }

    fun createService() {
        if (global_Service != null) return
        global_Service = PlayersListService()
    }

    override fun isPolling(): Boolean {
        return global_Service!!.isPolling()
    }

    override fun getPlayersList(): List<UserWithLastLoginResponse> {
        return global_Service!!.getPlayersList()
    }

    override fun setChatFragmentUpdateFunction(updateFunction: (List<UserWithLastLoginResponse>)->Unit) {
        global_Service!!.setChatFragmentUpdateFunction(updateFunction)
    }

    override fun deactivateUpdateOfChat() {
        global_Service!!.deactivateUpdateOfChat()
    }

    companion object {
        private var global_Service: PlayersListService? = null
    }
}