package com.planes.multiplayer_engine

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

    companion object {
        private var global_Round: MultiplayerRound? = null
        private var global_Guess_Result = Type.Miss
        private var global_Player_Guess_Reaction = PlayerGuessReaction()
    }
}