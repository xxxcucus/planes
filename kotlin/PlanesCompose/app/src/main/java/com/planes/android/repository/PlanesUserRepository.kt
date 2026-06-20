package com.planes.android.repository

import android.util.Log
import com.google.gson.JsonParser
import com.planes.android.data.DataOrError
import com.planes.android.network.user.PlanesUserApi
import com.planes.android.network.user.requests.DeleteUserRequest
import com.planes.android.network.user.requests.LoginRequest
import com.planes.android.network.user.requests.LogoutRequest
import com.planes.android.network.user.requests.NoRobotRequest
import com.planes.android.network.user.requests.RegistrationRequest
import com.planes.android.network.user.responses.DeleteUserResponse
import com.planes.android.network.user.responses.LoginResponse
import com.planes.android.network.user.responses.LoginResponseWithAuthorization
import com.planes.android.network.user.responses.LogoutResponse
import com.planes.android.network.user.responses.NoRobotResponse
import com.planes.android.network.user.responses.RegistrationResponse
import com.planes.android.network.version.VersionResponse
import com.planes.multiplayer_engine.requests.PlayersListRequest
import com.planes.multiplayer_engine.requests.ReceiveChatMessagesRequest
import com.planes.multiplayer_engine.requests.SendChatMessageRequest
import com.planes.multiplayer_engine.responses.PlayersListResponse
import com.planes.multiplayer_engine.responses.ReceiveChatMessagesResponse
import com.planes.multiplayer_engine.responses.SendChatMessageResponse
import retrofit2.Response
import javax.inject.Inject

class PlanesUserRepository @Inject constructor(private val api: PlanesUserApi) {
    suspend fun login(username: String, password: String): DataOrError<LoginResponseWithAuthorization> {


        var response: Response<LoginResponse>? = null

        try {
            response = api.login(LoginRequest(username, password))
        } catch (e: Exception) {
            return DataOrError<LoginResponseWithAuthorization>(null, false, e.message)
        }

        if (response.isSuccessful) {
            val authorizationHeader = response.headers().get("Authorization")
            return DataOrError(LoginResponseWithAuthorization(response.body()?.m_Id!!, response.body()?.m_Username!!, authorizationHeader!!),
                false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun logout(authorization: String, username: String, userid: String): DataOrError<LogoutResponse> {

        var response: Response<LogoutResponse>? = null

        try {
            response = api.logout(authorization, LogoutRequest(userid, username))
        } catch (e: Exception) {
            return DataOrError<LogoutResponse>(null, false, e.message)
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

    suspend fun deleteUser(authorization: String, username: String, userid: String): DataOrError<DeleteUserResponse> {

        var response: Response<DeleteUserResponse>? = null

        try {
            response = api.deactivateUser(authorization, DeleteUserRequest(userid, username))
        } catch (e: Exception) {
            return DataOrError<DeleteUserResponse>(null, false, e.message)
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

    suspend fun register(username: String, password: String): DataOrError<RegistrationResponse> {

        var response: Response<RegistrationResponse>? = null

        try {
            response = api.register(RegistrationRequest(username, password))
        } catch (e: Exception) {
            return DataOrError<RegistrationResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(),false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun noRobotRequest(request: NoRobotRequest): DataOrError<NoRobotResponse> {

        var response: Response<NoRobotResponse>? = null

        try {
            response = api.norobot(request)
        } catch (e: Exception) {
            return DataOrError<NoRobotResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(),false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun getPlayersList(authorization: String, request: PlayersListRequest): DataOrError<PlayersListResponse> {

        var response: Response<PlayersListResponse>? = null

        try {
            response = api.getPlayersList(authorization, request)
        } catch (e: Exception) {
            return DataOrError<PlayersListResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(),false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun getChatMessages(authorization: String, request: ReceiveChatMessagesRequest): DataOrError<ReceiveChatMessagesResponse> {

        var response: Response<ReceiveChatMessagesResponse>? = null

        try {
            response = api.getChatMessages(authorization, request)
        } catch (e: Exception) {
            return DataOrError<ReceiveChatMessagesResponse>(null, false, e.message)
        }


        if (response.isSuccessful) {
            return DataOrError(response.body(),false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }

    suspend fun sendChatMessage(authorization: String, request: SendChatMessageRequest): DataOrError<SendChatMessageResponse> {

        var response: Response<SendChatMessageResponse>? = null

        try {
            response = api.sendChatMessage(authorization, request)
        } catch (e: Exception) {
            return DataOrError<SendChatMessageResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(),false, null)
        } else {
            val errorString = response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }
}

