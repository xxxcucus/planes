package com.planes.android.repository

import com.google.gson.JsonParser
import com.planes.android.data.DataOrError
import com.planes.android.network.game.PlanesGameApi
import com.planes.android.network.user.PlanesUserApi
import com.planes.android.network.user.requests.LogoutRequest
import com.planes.android.network.user.responses.LogoutResponse
import com.planes.multiplayer_engine.requests.AcquireOpponentPositionsRequest
import com.planes.multiplayer_engine.requests.ConnectToGameRequest
import com.planes.multiplayer_engine.requests.CreateGameRequest
import com.planes.multiplayer_engine.requests.GameStatusRequest
import com.planes.multiplayer_engine.requests.SendNotSentMovesRequest
import com.planes.multiplayer_engine.requests.SendPlanePositionsRequest
import com.planes.multiplayer_engine.requests.SendWinnerRequest
import com.planes.multiplayer_engine.requests.StartNewRoundRequest
import com.planes.multiplayer_engine.responses.AcquireOpponentPositionsResponse
import com.planes.multiplayer_engine.responses.ConnectToGameResponse
import com.planes.multiplayer_engine.responses.CreateGameResponse
import com.planes.multiplayer_engine.responses.GameStatusResponse
import com.planes.multiplayer_engine.responses.SendChatMessageResponse
import com.planes.multiplayer_engine.responses.SendNotSentMovesResponse
import com.planes.multiplayer_engine.responses.SendPlanePositionsResponse
import com.planes.multiplayer_engine.responses.SendWinnerResponse
import com.planes.multiplayer_engine.responses.StartNewRoundResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import javax.inject.Inject

class PlanesGameRepository @Inject constructor(private val api: PlanesGameApi) {

    suspend fun gameStatus(authorization: String, gameStatusRequest: GameStatusRequest): DataOrError<GameStatusResponse> {

        var response: Response<GameStatusResponse>? = null

        try {
            response = api.refreshGameStatus(authorization, gameStatusRequest)
        } catch (e: Exception) {
            return DataOrError<GameStatusResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun connectToGame(authorization: String, connectToGameRequest: ConnectToGameRequest): DataOrError<ConnectToGameResponse> {

        var response: Response<ConnectToGameResponse>? = null

        try {
            response = api.connectToGame(authorization, connectToGameRequest)
        } catch (e: Exception) {
            return DataOrError<ConnectToGameResponse>(null, false, e.message)
        }


        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun createGame(authorization: String, createGameRequest: CreateGameRequest): DataOrError<CreateGameResponse> {

        var response: Response<CreateGameResponse>? = null

        try {
            response = api.createGame(authorization, createGameRequest)
        } catch (e: Exception) {
            return DataOrError<CreateGameResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun sendPlanePositions(authorization: String, sendPlanePositionsRequest: SendPlanePositionsRequest): DataOrError<SendPlanePositionsResponse> {

        var response: Response<SendPlanePositionsResponse>? = null

        try {
            response = api.sendPlanePositions(authorization, sendPlanePositionsRequest)
        } catch (e: Exception) {
            return DataOrError<SendPlanePositionsResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun acquireOpponentPlanePositions(authorization: String, acquireOpponentPositionsRequest: AcquireOpponentPositionsRequest): DataOrError<AcquireOpponentPositionsResponse> {

        var response: Response<AcquireOpponentPositionsResponse>? = null

        try {
            response = api.acquireOpponentPlanePositions(authorization, acquireOpponentPositionsRequest)
        } catch (e: Exception) {
            return DataOrError<AcquireOpponentPositionsResponse>(null, false, e.message)
        }


        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun sendOwnMove(authorization: String, sendNotSentMovesRequest: SendNotSentMovesRequest): DataOrError<SendNotSentMovesResponse> {

        var response: Response<SendNotSentMovesResponse>? = null

        try {
            response = api.sendOwnMove(authorization, sendNotSentMovesRequest)
        } catch (e: Exception) {
            return DataOrError<SendNotSentMovesResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun sendWinner(authorization: String, sendWinnerRequest: SendWinnerRequest): DataOrError<SendWinnerResponse> {

        var response: Response<SendWinnerResponse>? = null

        try {
            response = api.sendWinner(authorization, sendWinnerRequest)
        } catch (e: Exception) {
            return DataOrError<SendWinnerResponse>(null, false, e.message)
        }


        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun startNewRound(authorization: String, startNewRoundRequest: StartNewRoundRequest): DataOrError<StartNewRoundResponse> {

        var response: Response<StartNewRoundResponse>? = null

        try {
            response = api.startRound(authorization, startNewRoundRequest)
        } catch (e: Exception) {
            return DataOrError<StartNewRoundResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }
}