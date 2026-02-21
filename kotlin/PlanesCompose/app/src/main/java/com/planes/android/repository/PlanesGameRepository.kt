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
import com.planes.multiplayer_engine.responses.AcquireOpponentPositionsResponse
import com.planes.multiplayer_engine.responses.ConnectToGameResponse
import com.planes.multiplayer_engine.responses.CreateGameResponse
import com.planes.multiplayer_engine.responses.GameStatusResponse
import com.planes.multiplayer_engine.responses.SendNotSentMovesResponse
import com.planes.multiplayer_engine.responses.SendPlanePositionsResponse
import javax.inject.Inject

class PlanesGameRepository @Inject constructor(private val api: PlanesGameApi) {

    suspend fun gameStatus(authorization: String, gameStatusRequest: GameStatusRequest): DataOrError<GameStatusResponse> {
        val response = api.refreshGameStatus(authorization, gameStatusRequest)

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
        val response = api.connectToGame(authorization, connectToGameRequest)

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
        val response = api.createGame(authorization, createGameRequest)

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
        val response = api.sendPlanePositions(authorization, sendPlanePositionsRequest)

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
        val response = api.acquireOpponentPlanePositions(authorization, acquireOpponentPositionsRequest)

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
        val response = api.sendOwnMove(authorization, sendNotSentMovesRequest)

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