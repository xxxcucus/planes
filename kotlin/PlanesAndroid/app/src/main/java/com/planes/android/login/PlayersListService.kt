package com.planes.android.login

import com.planes.android.MultiplayerRoundInterface
import com.planes.android.R
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.PlayersListResponse
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.Vector
import java.util.concurrent.TimeUnit

class PlayersListService : IPlayersListService {
    private lateinit var m_PollPlayersListSubscription: Disposable
    private var m_PlaneRound = MultiplayerRoundJava()
    private lateinit var m_PlayersList: List<UserWithLastLoginResponse>
    private lateinit var m_ChatUpdateFunction: (List<UserWithLastLoginResponse>) -> Unit
    private var m_UpdateChat = false

    override fun startPolling() {

        m_PlaneRound.createPlanesRound()

        if (this::m_PollPlayersListSubscription.isInitialized && !m_PollPlayersListSubscription.isDisposed)
            return

        m_PlaneRound.createPlanesRound()
        m_PollPlayersListSubscription =
            Observable.interval(1,30, TimeUnit.SECONDS, Schedulers.io())
                .switchMap { m_PlaneRound.getPlayersList(90) }
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
        return !m_PollPlayersListSubscription.isDisposed && this::m_PollPlayersListSubscription.isInitialized
    }

    fun reactToPlayersListInPolling(body: PlayersListResponse?) {
        if (body == null)
            return;
        m_PlayersList = body.m_Usernames
        if (m_UpdateChat)
            m_ChatUpdateFunction(m_PlayersList)
    }

    override fun getPlayersList(): List<UserWithLastLoginResponse> {
        if (!this::m_PlayersList.isInitialized)
            return emptyList<UserWithLastLoginResponse>()
        return m_PlayersList
    }

    override fun setChatFragmentUpdateFunction(updateFunction: (List<UserWithLastLoginResponse>)->Unit) {
        m_UpdateChat = true
        m_ChatUpdateFunction = updateFunction
    }

    override fun deactivateUpdateOfChat() {
        m_UpdateChat = false
    }
}