package com.planes.android.repository

import com.google.gson.JsonParser
import com.planes.android.data.DataOrError
import com.planes.android.network.game.PlanesGameApi
import com.planes.android.network.game.requests.AcquireOpponentPositionsRequest
import com.planes.android.network.game.requests.ConnectToGameRequest
import com.planes.android.network.game.requests.CreateGameRequest
import com.planes.android.network.game.requests.GameStatusRequest
import com.planes.android.network.game.requests.SendNotSentMovesRequest
import com.planes.android.network.game.requests.SendPlanePositionsRequest
import com.planes.android.network.game.requests.SendWinnerRequest
import com.planes.android.network.game.requests.StartNewRoundRequest
import com.planes.android.network.game.responses.AcquireOpponentPositionsResponse
import com.planes.android.network.game.responses.ConnectToGameResponse
import com.planes.android.network.game.responses.CreateGameResponse
import com.planes.android.network.game.responses.GameStatusResponse
import com.planes.android.network.game.responses.SendNotSentMovesResponse
import com.planes.android.network.game.responses.SendPlanePositionsResponse
import com.planes.android.network.game.responses.SendWinnerResponse
import com.planes.android.network.game.responses.StartNewRoundResponse
import retrofit2.Response
import javax.inject.Inject

class PlanesGameRepository @Inject constructor(private val api: PlanesGameApi) {

    suspend fun gameStatus(authorization: String, gameStatusRequest: GameStatusRequest): DataOrError<GameStatusResponse> {

        var response: Response<GameStatusResponse>?

        try {
            response = api.refreshGameStatus(authorization, gameStatusRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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

        var response: Response<ConnectToGameResponse>?

        try {
            response = api.connectToGame(authorization, connectToGameRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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

        var response: Response<CreateGameResponse>?

        try {
            response = api.createGame(authorization, createGameRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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

        var response: Response<SendPlanePositionsResponse>?

        try {
            response = api.sendPlanePositions(authorization, sendPlanePositionsRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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

        var response: Response<AcquireOpponentPositionsResponse>?

        try {
            response = api.acquireOpponentPlanePositions(authorization, acquireOpponentPositionsRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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

        var response: Response<SendNotSentMovesResponse>?

        try {
            response = api.sendOwnMove(authorization, sendNotSentMovesRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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

        var response: Response<SendWinnerResponse>?

        try {
            response = api.sendWinner(authorization, sendWinnerRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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

        var response: Response<StartNewRoundResponse>?

        try {
            response = api.startRound(authorization, startNewRoundRequest)
        } catch (e: Exception) {
            return DataOrError(null, false, e.message)
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