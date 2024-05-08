package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.requests.*
import com.planes.multiplayer_engine.responses.*
import com.planes.multiplayer_engine.responses.SendNotSentMovesResponse
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

    @POST("operations/logout")
    @Headers("Content-Type: application/json")
    fun logout(@Header("Authorization") authorization: String, @Body user: LogoutRequest): Observable<retrofit2.Response<LogoutResponse>>

    @POST("users/deactivate_user")
    @Headers("Content-Type: application/json")
    fun deactivateUser(@Header("Authorization") authorization: String, @Body user: DeleteUserRequest): Observable<retrofit2.Response<DeleteUserResponse>>

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


    @POST("round/myplanespositions")
    @Headers("Content-Type: application/json")
    fun sendPlanePositions(@Header("Authorization") authorization: String, @Body positions: SendPlanePositionsRequest): Observable<retrofit2.Response<SendPlanePositionsResponse>>

    @POST("round/otherplanespositions")
    @Headers("Content-Type: application/json")
    fun acquireOpponentPlanePositions(@Header("Authorization") authorization: String, @Body request: AcquireOpponentPositionsRequest): Observable<retrofit2.Response<AcquireOpponentPositionsResponse>>

    @POST("round/end")
    @Headers("Content-Type: application/json")
    fun sendWinner(@Header("Authorization") authorization: String, @Body request: SendWinnerRequest): Observable<retrofit2.Response<SendWinnerResponse>>

    @POST("round/mymove")
    @Headers("Content-Type: application/json")
    fun sendOwnMove(@Header("Authorization") authorization: String, @Body request: SendNotSentMovesRequest): Observable<retrofit2.Response<SendNotSentMovesResponse>>

    @POST("round/cancel")
    @Headers("Content-Type: application/json")
    fun cancelRound(@Header("Authorization") authorization: String, @Body request: CancelRoundRequest): Observable<retrofit2.Response<CancelRoundResponse>>

    @POST("round/start")
    @Headers("Content-Type: application/json")
    fun startRound(@Header("Authorization") authorization: String, @Body request: StartNewRoundRequest): Observable<retrofit2.Response<StartNewRoundResponse>>

    @POST("users/available_users")
    @Headers("Content-Type: application/json")
    fun getPlayersList(@Header("Authorization") authorization: String, @Body request: PlayersListRequest): Observable<retrofit2.Response<PlayersListResponse>>

}