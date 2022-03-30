package com.planes.android

import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.requests.NoRobotRequest
import com.planes.multiplayer_engine.responses.*
import io.reactivex.Observable
import retrofit2.Response

interface MultiplayerRoundInterface {
    fun createPlanesRound()

    fun testServerVersion(): Observable<Response<VersionResponse>>

    fun login(username: String, password: String): Observable<Response<LoginResponse>>

    fun setUserData(username: String, password: String, authToken: String)

    fun getUsername(): String

    fun register(username: String, password: String): Observable<Response<RegistrationResponse>>

    fun setRegistrationResponse(regResp: RegistrationResponse)

    fun getRegistrationResponse(): RegistrationResponse

    fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>>

    fun isUserLoggedIn(): Boolean

    fun refreshGameStatus(gameName: String): Observable<retrofit2.Response<GameStatusResponse>>
}