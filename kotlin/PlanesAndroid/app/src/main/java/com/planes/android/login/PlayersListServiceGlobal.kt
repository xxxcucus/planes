package com.planes.android.login

class PlayersListServiceGlobal : IPlayersListService {

    override fun startPolling() {
        global_Service!!.startPolling()
    }
    override fun stopPolling() {
        global_Service!!.stopPolling()
    }

    fun createService() {
        if (PlayersListServiceGlobal.global_Service != null) return
        PlayersListServiceGlobal.global_Service = PlayersListService()
    }

    override fun isPolling(): Boolean {
        return global_Service!!.isPolling()
    }

    override fun getPlayersList(): List<String> {
        return global_Service!!.getPlayersList()
    }

    override fun setChatFragmentUpdateFunction(updateFunction: (List<String>)->Unit) {
        global_Service!!.setChatFragmentUpdateFunction(updateFunction)
    }

    override fun deactivateUpdateOfChat() {
        global_Service!!.deactivateUpdateOfChat()
    }

    companion object {
        private var global_Service: PlayersListService? = null
    }
}