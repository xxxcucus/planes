package com.planes.multiplayer_engine

import com.planes.android.MultiplayerRoundInterface
import com.planes.multiplayer_engine.responses.LoginResponse
import com.planes.multiplayer_engine.responses.RegistrationResponse
import com.planes.multiplayer_engine.responses.VersionResponse
import com.planes.single_player_engine.PlaneRound
import com.planes.single_player_engine.PlanesRoundJava
import com.planes.single_player_engine.PlayerGuessReaction
import com.planes.single_player_engine.Type
import io.reactivex.Observable
import retrofit2.Response

class MultiplayerRoundJava : MultiplayerRoundInterface {
    override fun createPlanesRound() {
        if (MultiplayerRoundJava.global_Round != null) return
        MultiplayerRoundJava.global_Round = MultiplayerRound()
    }

    override fun testServerVersion(): Observable<Response<VersionResponse>> {
        return global_Round!!.testServerVersion()
    }

    override fun login(username: String, password: String): Observable<Response<LoginResponse>> {
        return global_Round!!.login(username, password)
    }

    override fun setUserData(username: String, password: String, authToken: String) {
        global_Round!!.setUserData(username, password, authToken)
    }

    override fun getUsername(): String {
        return global_Round!!.getUsername()
    }

    override fun register(username: String, password: String): Observable<Response<RegistrationResponse>> {
        return global_Round!!.register(username, password)
    }

    override fun setRegistrationResponse(regResp: RegistrationResponse) {
        global_Round!!.setRegistrationResponse(regResp)
    }

    override fun getRegistrationResponse(): RegistrationResponse {
        return global_Round!!.getRegistrationResponse()
    }


    companion object {
        private var global_Round: MultiplayerRound? = null
        private var global_Guess_Result = Type.Miss
        private var global_Player_Guess_Reaction = PlayerGuessReaction()
    }
}