package com.planes.android.login

interface IPlayersListService {
    fun startPolling()
    fun stopPolling()

    fun isPolling(): Boolean

    fun getPlayersList(): List<String>

    fun setChatFragmentUpdateFunction(updateFunction: (List<String>)->Unit)

    fun deactivateUpdateOfChat()
}