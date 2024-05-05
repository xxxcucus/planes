package com.planes.android.login

interface IPlayersListService {
    fun startPolling()
    fun stopPolling()

    fun isPolling(): Boolean

    fun getPlayersList(): List<String>
}