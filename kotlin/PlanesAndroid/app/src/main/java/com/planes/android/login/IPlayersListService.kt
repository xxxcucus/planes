package com.planes.android.login

import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse

interface IPlayersListService {
    fun startPolling()
    fun stopPolling()

    fun isPolling(): Boolean

    fun getPlayersList(): List<UserWithLastLoginResponse>

    fun setChatFragmentUpdateFunction(updateFunction: (List<UserWithLastLoginResponse>)->Unit)

    fun deactivateUpdateOfChat()
}