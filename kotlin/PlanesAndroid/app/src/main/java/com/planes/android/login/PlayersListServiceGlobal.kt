package com.planes.android.login

class PlayersListServiceGlobal : IPlayersListService {

    override fun startPolling() {
        global_Service!!.startPolling()
    }
    override fun stopPolling() {
        global_Service!!.stopPolling()
    }

    fun createPreferencesService() {
        if (PlayersListServiceGlobal.global_Service != null) return
        PlayersListServiceGlobal.global_Service = PlayersListService()
    }
    companion object {
        private var global_Service: PlayersListService? = null
    }
}