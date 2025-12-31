package com.planes.android.network.game

import com.planes.multiplayer_engine.requests.AcquireOpponentPositionsRequest
import com.planes.multiplayer_engine.requests.CancelRoundRequest
import com.planes.multiplayer_engine.requests.ConnectToGameRequest
import com.planes.multiplayer_engine.requests.CreateGameRequest
import com.planes.multiplayer_engine.requests.GameStatusRequest
import com.planes.multiplayer_engine.requests.SendNotSentMovesRequest
import com.planes.multiplayer_engine.requests.SendPlanePositionsRequest
import com.planes.multiplayer_engine.requests.SendWinnerRequest
import com.planes.multiplayer_engine.requests.StartNewRoundRequest
import com.planes.multiplayer_engine.responses.AcquireOpponentPositionsResponse
import com.planes.multiplayer_engine.responses.CancelRoundResponse
import com.planes.multiplayer_engine.responses.ConnectToGameResponse
import com.planes.multiplayer_engine.responses.CreateGameResponse
import com.planes.multiplayer_engine.responses.GameStatusResponse
import com.planes.multiplayer_engine.responses.SendNotSentMovesResponse
import com.planes.multiplayer_engine.responses.SendPlanePositionsResponse
import com.planes.multiplayer_engine.responses.SendWinnerResponse
import com.planes.multiplayer_engine.responses.StartNewRoundResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PlanesGameApi {
    @POST("game/status")
    @Headers("Content-Type: application/json")
    suspend fun refreshGameStatus(@Header("Authorization") authorization: String, @Body game: GameStatusRequest): Response<GameStatusResponse>

    @POST("game/create")
    @Headers("Content-Type: application/json")
    fun createGame(@Header("Authorization") authorization: String, @Body game: CreateGameRequest): Response<CreateGameResponse>

    @POST("game/connect")
    @Headers("Content-Type: application/json")
    fun connectToGame(@Header("Authorization") authorization: String, @Body game: ConnectToGameRequest): Response<ConnectToGameResponse>

    @POST("round/myplanespositions")
    @Headers("Content-Type: application/json")
    fun sendPlanePositions(@Header("Authorization") authorization: String, @Body positions: SendPlanePositionsRequest): Response<SendPlanePositionsResponse>

    @POST("round/otherplanespositions")
    @Headers("Content-Type: application/json")
    fun acquireOpponentPlanePositions(@Header("Authorization") authorization: String, @Body request: AcquireOpponentPositionsRequest): Response<AcquireOpponentPositionsResponse>

    @POST("round/end")
    @Headers("Content-Type: application/json")
    fun sendWinner(@Header("Authorization") authorization: String, @Body request: SendWinnerRequest): Response<SendWinnerResponse>

    @POST("round/mymove")
    @Headers("Content-Type: application/json")
    fun sendOwnMove(@Header("Authorization") authorization: String, @Body request: SendNotSentMovesRequest): Response<SendNotSentMovesResponse>

    @POST("round/cancel")
    @Headers("Content-Type: application/json")
    fun cancelRound(@Header("Authorization") authorization: String, @Body request: CancelRoundRequest): Response<CancelRoundResponse>

    @POST("round/start")
    @Headers("Content-Type: application/json")
    fun startRound(@Header("Authorization") authorization: String, @Body request: StartNewRoundRequest): Response<StartNewRoundResponse>

}