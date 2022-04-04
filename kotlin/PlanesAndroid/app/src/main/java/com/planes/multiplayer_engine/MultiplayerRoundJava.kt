package com.planes.multiplayer_engine

import com.planes.android.MultiplayerRoundInterface
import com.planes.multiplayer_engine.requests.CreateGameRequest
import com.planes.multiplayer_engine.requests.NoRobotRequest
import com.planes.multiplayer_engine.responses.*
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

    override fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>> {
        return global_Round!!.norobot(requestId, answer);
    }

    override fun isUserLoggedIn(): Boolean {
        return global_Round!!.isUserLoggedIn()
    }

    override fun refreshGameStatus(gameName: String):
            Observable<retrofit2.Response<GameStatusResponse>> {
        return global_Round!!.refreshGameStatus(gameName)
    }

    override fun createGame(gameName: String): Observable<retrofit2.Response<CreateGameResponse>> {
        return global_Round!!.createGame(gameName)
    }

    override fun setGameData(gameCreationResponse: CreateGameResponse) {
        return global_Round!!.setGameData(gameCreationResponse)
    }

    override fun setGameData(connectToGameResponse: ConnectToGameResponse) {
        return global_Round!!.setGameData(connectToGameResponse)
    }

    override fun setUserId(userid: Long) {
        return global_Round!!.setUserId(userid)
    }

    override fun connectToGame(gameName: String): Observable<retrofit2.Response<ConnectToGameResponse>> {
        return global_Round!!.connectToGame(gameName)
    }

    companion object {
        private var global_Round: MultiplayerRound? = null
        private var global_Guess_Result = Type.Miss
        private var global_Player_Guess_Reaction = PlayerGuessReaction()
    }
}