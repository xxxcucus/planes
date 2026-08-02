package com.planes.android.repository

import com.google.gson.JsonParser
import com.planes.android.data.DataOrError
import com.planes.android.network.version.PlanesVersionApi
import com.planes.android.network.version.VersionResponse
import retrofit2.Response
import javax.inject.Inject

class PlanesVersionRepository @Inject constructor(private val api: PlanesVersionApi) {
    suspend fun getVersion(): DataOrError<VersionResponse> {

        var response: Response<VersionResponse>? = null

        try {
            response = api.getVersion()
        } catch (e: Exception) {
            return DataOrError<VersionResponse>(null, false, e.message)
        }

        if (response.isSuccessful) {
            return DataOrError(response.body(), false, null)
        } else {
            val errorString =
                response.errorBody()?.string() ?: return DataOrError(null, false, null)
            val message = JsonParser.parseString(errorString).asJsonObject["message"].asString
            val status = response.code()
            return DataOrError(null, false, "Error $message with status code $status")
        }
    }
}