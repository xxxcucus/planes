package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.requests.LoginRequest
import com.planes.multiplayer_engine.responses.LoginResponse
import com.planes.multiplayer_engine.responses.VersionResponse
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import org.mindrot.jbcrypt.BCrypt
import okhttp3.Interceptor

import okhttp3.OkHttpClient




class MultiplayerRound {
    private lateinit var m_Service: MultiplayerCommApi



    companion object {

        private const val OK_HTTP_CLIENT_TIMEOUT: Long = 60

        private val HTTP_LOGGING_INTERCEPTOR = HttpLoggingInterceptor()
        //TODO to adapt this to login requirements
        private val HTTP_HEADERS = constructHeaderInterceptor()

        private const val HTTP_ORIGIN_HEADER = "Origin"
        private const val HTTP_ORIGIN_VALUE = "Android"

        private fun constructHeaderInterceptor(): Interceptor {
            return Interceptor {
                /*val request = it.request()
                val newRequest = request.newBuilder().addHeader(HTTP_ORIGIN_HEADER, HTTP_ORIGIN_VALUE).build()
                it.proceed(newRequest)*/

                val requestBuilder = it.request().newBuilder()
                //requestBuilder.header("Content-Type", "application/json")
                //requestBuilder.header("Accept", "application/json")
                it.proceed(requestBuilder.build())
            }
        }

        init {
            HTTP_LOGGING_INTERCEPTOR.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    init {
        var spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build()
        var okHttpClient = OkHttpClient.Builder()
            .connectTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .addInterceptor(HTTP_HEADERS)
            //.addInterceptor(HTTP_LOGGING_INTERCEPTOR)
            .connectionSpecs(Collections.singletonList(spec))
            .build()


        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://planes.planes-android.com:8443/planesserver/")
            .build()
        m_Service = retrofit.create((MultiplayerCommApi::class.java))
    }

    //TODO: have to wait one second for server reply and when it does not come
    //give error

    fun testServerVersion(): Observable<Response<VersionResponse>> {
        return m_Service.getVersion()
    }

    fun login(username: String, password: String): Observable<Response<LoginResponse>> {

        //val bchash = BCrypt.hashpw(password, BCrypt.gensalt())
        //return m_Service.login(LoginRequest(username, bchash))
        return m_Service.login(LoginRequest(username, password))
    }
}

