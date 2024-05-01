package com.planes.android.login

import com.planes.android.MultiplayerRoundInterface
import com.planes.android.R
import com.planes.multiplayer_engine.responses.PlayersListResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.Vector
import java.util.concurrent.TimeUnit

class PlayersListService : IPlayersListService {
    private lateinit var m_PollPlayersListSubscription: Disposable
    private lateinit var m_PlaneRound: MultiplayerRoundInterface
    private lateinit var m_PlayersList: List<String>

    override fun startPolling() {
        if (this::m_PollPlayersListSubscription.isInitialized)
            return

        m_PlaneRound.createPlanesRound()
        m_PollPlayersListSubscription =
            Observable.interval(30, TimeUnit.SECONDS, Schedulers.io())
                .flatMap { m_PlaneRound.getPlayersList() }
                //.doOnError { setReceiveOpponentPlanePositionsError(getString(R.string.error_plane_positions)) }
                .retry()
                .observeOn(Schedulers.io())
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

    fun reactToPlayersListInPolling(body: PlayersListResponse?) {
        if (body == null)
            return;
        m_PlayersList = body.m_Users.map { user -> user.m_Username }
    }

    fun getPlayersList(): List<String> {
        if (!this::m_PlayersList.isInitialized)
            return emptyList<String>()
        return m_PlayersList
    }
}