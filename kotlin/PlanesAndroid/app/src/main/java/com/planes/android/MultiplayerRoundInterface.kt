package com.planes.android

import com.planes.multiplayer_engine.responses.LoginResponse
import com.planes.multiplayer_engine.responses.VersionResponse
import io.reactivex.Observable
import retrofit2.Response

interface MultiplayerRoundInterface {
    fun createPlanesRound()

    fun testServerVersion(): Observable<Response<VersionResponse>>

    fun login(username: String, password: String): Observable<Response<LoginResponse>>

    fun setUserData(username: String, password: String, authToken: String)

    fun getUsername(): String

}