package com.planes.multiplayer_engine

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface MultiplayerCommService {

    companion object Factory {
        private var service: MultiplayerCommService? = null

        fun getInstance(): MultiplayerCommService {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://planes.planes-android.com:8443/planesserver")
                    .build()
                service = retrofit.create((MultiplayerCommService::class.java))
            }

            return service as MultiplayerCommService
        }
    }
}