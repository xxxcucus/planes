package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.responses.VersionResponse
import retrofit2.http.Headers
import retrofit2.http.POST
import io.reactivex.Observable

interface MultiplayerCommApi {
    @POST("status/getversion")
    @Headers("Content-Type: application/json")
    fun getVersion(): Observable<retrofit2.Response<VersionResponse>>
}