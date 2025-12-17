package com.planes.android.repository

import android.util.Log
import com.google.gson.JsonParser
import com.planes.android.data.DataOrError
import com.planes.android.network.user.PlanesUserApi
import com.planes.android.network.user.requests.LoginRequest
import com.planes.android.network.user.responses.LoginResponse
import com.planes.android.network.user.responses.LoginResponseWithAuthorization
import javax.inject.Inject

class PlanesUserRepository @Inject constructor(private val api: PlanesUserApi) {
    suspend fun login(username: String, password: String): DataOrError<LoginResponseWithAuthorization> {
        val response = api.login(LoginRequest(username, password))

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
}