package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.requests.LoginRequest
import com.planes.multiplayer_engine.responses.LoginResponse
import com.planes.multiplayer_engine.responses.VersionResponse
import retrofit2.http.Headers
import retrofit2.http.POST
import io.reactivex.Observable
import retrofit2.http.Body

interface MultiplayerCommApi {
    @POST("status/getversion")
    @Headers("Content-Type: application/json")
    fun getVersion(): Observable<retrofit2.Response<VersionResponse>>

    @POST("login")
    @Headers("Content-Type: application/json")
    fun login(@Body user: LoginRequest): Observable<retrofit2.Response<LoginResponse>>
}