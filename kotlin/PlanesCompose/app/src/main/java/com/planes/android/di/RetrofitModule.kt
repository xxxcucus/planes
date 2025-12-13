package com.planes.android.di

import com.planes.android.network.user.PlanesUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun providePlanesUserApi(): PlanesUserApi {

        val baseUrl = "https://planes.planes-android.com:8443/planesserver/"
        val okHttpClient = buildOkHttpClient()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
        return retrofit.create(PlanesUserApi::class.java)
    }

    fun buildOkHttpClient() : OkHttpClient {
        val OK_HTTP_CLIENT_TIMEOUT: Long = 300

        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            )
            .build()
        return  OkHttpClient.Builder()
            .connectTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            //.addInterceptor(HTTP_HEADERS)
            //.addInterceptor(HTTP_LOGGING_INTERCEPTOR)
            .connectionSpecs(Collections.singletonList(spec))
            .build()
    }
}