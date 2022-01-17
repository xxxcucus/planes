package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.responses.VersionResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MultiplayerRound {
    private lateinit var m_Service: MultiplayerCommApi

    init {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://planes.planes-android.com:8443/planesserver/")
            .build()
        m_Service = retrofit.create((MultiplayerCommApi::class.java))
    }

    //TODO: have to wait one second for server reply and when it does not come
    //give error

    fun testServerVersion(): Observable<Response<VersionResponse>> {
        return m_Service.getVersion()
    }
}

