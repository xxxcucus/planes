package com.planes.android.network.game

import com.planes.android.network.game.requests.AcquireOpponentPositionsRequest
import com.planes.android.network.game.requests.CancelRoundRequest
import com.planes.android.network.game.requests.ConnectToGameRequest
import com.planes.android.network.game.requests.CreateGameRequest
import com.planes.android.network.game.requests.GameStatusRequest
import com.planes.android.network.game.requests.SendNotSentMovesRequest
import com.planes.android.network.game.requests.SendPlanePositionsRequest
import com.planes.android.network.game.requests.SendWinnerRequest
import com.planes.android.network.game.requests.StartNewRoundRequest
import com.planes.android.network.game.responses.AcquireOpponentPositionsResponse
import com.planes.android.network.game.responses.CancelRoundResponse
import com.planes.android.network.game.responses.ConnectToGameResponse
import com.planes.android.network.game.responses.CreateGameResponse
import com.planes.android.network.game.responses.GameStatusResponse
import com.planes.android.network.game.responses.SendNotSentMovesResponse
import com.planes.android.network.game.responses.SendPlanePositionsResponse
import com.planes.android.network.game.responses.SendWinnerResponse
import com.planes.android.network.game.responses.StartNewRoundResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface PlanesGameApi {
    @POST("game/status")
    @Headers("Content-Type: application/json")
    suspend fun refreshGameStatus(@Header("Authorization") authorization: String, @Body game: GameStatusRequest): Response<GameStatusResponse>

    @POST("game/create")
    @Headers("Content-Type: application/json")
    suspend fun createGame(@Header("Authorization") authorization: String, @Body game: CreateGameRequest): Response<CreateGameResponse>

    @POST("game/connect")
    @Headers("Content-Type: application/json")
    suspend fun connectToGame(@Header("Authorization") authorization: String, @Body game: ConnectToGameRequest): Response<ConnectToGameResponse>

    @POST("round/myplanespositions")
    @Headers("Content-Type: application/json")
    suspend fun sendPlanePositions(@Header("Authorization") authorization: String, @Body positions: SendPlanePositionsRequest): Response<SendPlanePositionsResponse>

    @POST("round/otherplanespositions")
    @Headers("Content-Type: application/json")
    suspend fun acquireOpponentPlanePositions(@Header("Authorization") authorization: String, @Body request: AcquireOpponentPositionsRequest): Response<AcquireOpponentPositionsResponse>

    @POST("round/end")
    @Headers("Content-Type: application/json")
    suspend fun sendWinner(@Header("Authorization") authorization: String, @Body request: SendWinnerRequest): Response<SendWinnerResponse>

    @POST("round/mymove")
    @Headers("Content-Type: application/json")
    suspend fun sendOwnMove(@Header("Authorization") authorization: String, @Body request: SendNotSentMovesRequest): Response<SendNotSentMovesResponse>

    @POST("round/cancel")
    @Headers("Content-Type: application/json")
    suspend fun cancelRound(@Header("Authorization") authorization: String, @Body request: CancelRoundRequest): Response<CancelRoundResponse>

    @POST("round/start")
    @Headers("Content-Type: application/json")
    suspend fun startRound(@Header("Authorization") authorization: String, @Body request: StartNewRoundRequest): Response<StartNewRoundResponse>

}