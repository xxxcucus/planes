package com.planes.android.network.version

import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface PlanesVersionApi {
    @POST("status/getversion")
    @Headers("Content-Type: application/json")
    suspend fun getVersion(): Response<VersionResponse>
}

