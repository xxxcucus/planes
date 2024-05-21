package com.planes.android.login

import com.planes.android.MultiplayerRoundInterface
import com.planes.android.R
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.PlayersListResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.Vector
import java.util.concurrent.TimeUnit

class PlayersListService : IPlayersListService {
    private lateinit var m_PollPlayersListSubscription: Disposable
    private var m_PlaneRound = MultiplayerRoundJava()
    private lateinit var m_PlayersList: List<String>
    private lateinit var m_ChatUpdateFunction: (List<String>) -> Unit
    private var m_UpdateChat = false

    override fun startPolling() {

        m_PlaneRound.createPlanesRound()

        if (this::m_PollPlayersListSubscription.isInitialized)
            return

        m_PlaneRound.createPlanesRound()
        m_PollPlayersListSubscription =
            Observable.interval(30, TimeUnit.SECONDS, Schedulers.io())
                .flatMap { m_PlaneRound.getPlayersList() }
                //.doOnError { setReceiveOpponentPlanePositionsError(getString(R.string.error_plane_positions)) }
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> reactToPlayersListInPolling(data.body()) }
                ) { }
    }

    override fun stopPolling() {
        destroySubscription()
    }

    fun destroySubscription() {
        if (this::m_PollPlayersListSubscription.isInitialized)
            m_PollPlayersListSubscription.dispose()
    }

    override fun isPolling(): Boolean {
        return this::m_PollPlayersListSubscription.isInitialized
    }

    fun reactToPlayersListInPolling(body: PlayersListResponse?) {
        if (body == null)
            return;
        m_PlayersList = body.m_Usernames
        if (m_UpdateChat)
            m_ChatUpdateFunction(m_PlayersList)
    }

    override fun getPlayersList(): List<String> {
        if (!this::m_PlayersList.isInitialized)
            return emptyList<String>()
        return m_PlayersList
    }

    override fun setChatFragmentUpdateFunction(updateFunction: (List<String>)->Unit) {
        m_UpdateChat = true
        m_ChatUpdateFunction = updateFunction
    }

    override fun deactivateUpdateOfChat() {
        m_UpdateChat = false
    }
}