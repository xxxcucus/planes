package com.planes.android

import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.requests.CreateGameRequest
import com.planes.multiplayer_engine.requests.NoRobotRequest
import com.planes.multiplayer_engine.responses.*
import io.reactivex.Observable
import retrofit2.Response

interface MultiplayerRoundInterface {
    fun createPlanesRound()

    fun testServerVersion(): Observable<Response<VersionResponse>>

    fun login(username: String, password: String): Observable<Response<LoginResponse>>

    fun setUserData(username: String, password: String, authToken: String)

    fun getGameId() : Long

    fun getRoundId() : Long

    fun getUserId(): Long

    fun getOpponentId(): Long

    fun getUsername(): String

    fun getOpponentName(): String

    fun register(username: String, password: String): Observable<Response<RegistrationResponse>>

    fun setRegistrationResponse(regResp: RegistrationResponse)

    fun getRegistrationResponse(): RegistrationResponse

    fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>>

    fun isUserLoggedIn(): Boolean

    fun refreshGameStatus(gameName: String): Observable<retrofit2.Response<GameStatusResponse>>

    fun createGame(gameName: String): Observable<retrofit2.Response<CreateGameResponse>>

    fun setGameData(gameCreationResponse: CreateGameResponse)

    fun setGameData(connectToGameResponse: ConnectToGameResponse)

    fun setGameData(gameStatusResponse: GameStatusResponse)

    fun setUserId(userid: Long)

    fun connectToGame(gameName: String): Observable<retrofit2.Response<ConnectToGameResponse>>

    fun resetGameData()
}