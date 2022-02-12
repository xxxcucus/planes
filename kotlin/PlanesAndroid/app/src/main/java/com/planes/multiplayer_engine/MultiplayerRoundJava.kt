package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.responses.LoginResponse
import com.planes.multiplayer_engine.responses.VersionResponse
import com.planes.single_player_engine.PlaneRound
import com.planes.single_player_engine.PlanesRoundJava
import com.planes.single_player_engine.PlayerGuessReaction
import com.planes.single_player_engine.Type
import io.reactivex.Observable
import retrofit2.Response

class MultiplayerRoundJava {
    fun createPlanesRound() {
        if (MultiplayerRoundJava.global_Round != null) return
        MultiplayerRoundJava.global_Round = MultiplayerRound()
    }

    fun testServerVersion(): Observable<Response<VersionResponse>> {
        return global_Round!!.testServerVersion()
    }

    fun login(username: String, password: String): Observable<Response<LoginResponse>> {
        return global_Round!!.login(username, password)
    }

    fun setUserData(username: String, password: String, authToken: String) {
        global_Round!!.setUserData(username, password, authToken)
    }

    companion object {
        private var global_Round: MultiplayerRound? = null
        private var global_Guess_Result = Type.Miss
        private var global_Player_Guess_Reaction = PlayerGuessReaction()
    }
}