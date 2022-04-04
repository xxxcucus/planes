package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.requests.*
import com.planes.multiplayer_engine.responses.*
import retrofit2.http.Headers
import retrofit2.http.POST
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Header

interface MultiplayerCommApi {
    @POST("status/getversion")
    @Headers("Content-Type: application/json")
    fun getVersion(): Observable<retrofit2.Response<VersionResponse>>

    @POST("login/")
    @Headers("Content-Type: application/json")
    fun login(@Body user: LoginRequest): Observable<retrofit2.Response<LoginResponse>>

    @POST("users/registration_request")
    @Headers("Content-Type: application/json")
    fun register(@Body user: RegistrationRequest): Observable<retrofit2.Response<RegistrationResponse>>

    @POST("users/registration_confirm")
    @Headers("Content-Type: application/json")
    fun norobot(@Body user: NoRobotRequest): Observable<retrofit2.Response<NoRobotResponse>>

    @POST("game/status")
    @Headers("Content-Type: application/json")
    fun refreshGameStatus(@Header("Authorization") authorization: String, @Body game: GameStatusRequest): Observable<retrofit2.Response<GameStatusResponse>>

    @POST("game/create")
    @Headers("Content-Type: application/json")
    fun createGame(@Header("Authorization") authorization: String, @Body game: CreateGameRequest): Observable<retrofit2.Response<CreateGameResponse>>

    @POST("game/connect")
    @Headers("Content-Type: application/json")
    fun connectToGame(@Header("Authorization") authorization: String, @Body game: ConnectToGameRequest): Observable<retrofit2.Response<ConnectToGameResponse>>
}