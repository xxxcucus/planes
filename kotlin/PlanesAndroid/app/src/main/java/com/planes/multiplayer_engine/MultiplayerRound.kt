package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.requests.*
import com.planes.multiplayer_engine.responses.*
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
import retrofit2.http.Body


class MultiplayerRound {
    private lateinit var m_Service: MultiplayerCommApi
    private var m_GameData: GameData = GameData()
    private var m_UserData: UserData = UserData()
    private lateinit var m_RegistrationData: RegistrationResponse
    private val OK_HTTP_CLIENT_TIMEOUT: Long = 60
    private val HTTP_LOGGING_INTERCEPTOR = HttpLoggingInterceptor()
    //TODO to adapt this to login requirements
    private val HTTP_HEADERS = constructHeaderInterceptor()
    private val HTTP_ORIGIN_HEADER = "Origin"
    private val HTTP_ORIGIN_VALUE = "Android"

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
            //.addInterceptor(HTTP_HEADERS)
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

    fun testServerVersion(): Observable<Response<VersionResponse>> {
        return m_Service.getVersion()
    }

    fun login(username: String, password: String): Observable<Response<LoginResponse>> {

        return m_Service.login(LoginRequest(username, password))
    }

    fun setUserData(username: String, password: String, authToken: String) {
        m_UserData.userName = username
        m_UserData.password = password
        m_UserData.authToken = authToken
    }

    fun getUsername() : String {
        return m_UserData.userName
    }

    fun register(username: String, password: String): Observable<Response<RegistrationResponse>> {
        val bchash = BCrypt.hashpw(password, BCrypt.gensalt())
        return m_Service.register(RegistrationRequest(username, bchash))
    }

    fun setRegistrationResponse(regResp: RegistrationResponse) {
        m_RegistrationData = regResp
    }

    fun getRegistrationResponse(): RegistrationResponse {
        return m_RegistrationData
    }

    fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>> {
        return m_Service.norobot(NoRobotRequest(requestId.toString(), answer));
    }

    fun isUserLoggedIn(): Boolean {
        return !m_UserData.userName.isNullOrEmpty() && !m_UserData.authToken.isNullOrEmpty()
    }

    fun refreshGameStatus(gameName: String/*, gameId: String, userName: String, userId: String*/):
            Observable<retrofit2.Response<GameStatusResponse>> {
        return m_Service.refreshGameStatus(m_UserData.authToken,
            GameStatusRequest(gameName, m_UserData.userName, m_UserData.userId.toString() , m_GameData.gameId.toString()))
    }

    fun createGame(gameName: String): Observable<retrofit2.Response<CreateGameResponse>> {
        return m_Service.createGame(m_UserData.authToken,
            CreateGameRequest(gameName, m_UserData.userName, m_UserData.userId.toString() , m_GameData.gameId.toString())
        )
    }

    fun setGameData(gameCreationResponse: CreateGameResponse) {
        m_GameData.setFromCreateGameResponse(gameCreationResponse)
    }

    fun setGameData(connectToGameResponse: ConnectToGameResponse) {
        m_GameData.setFromConnectToGameResponse(connectToGameResponse)
    }

    fun setUserId(userid: Long) {
        m_UserData.userId = userid
    }

    fun connectToGame(gameName: String): Observable<retrofit2.Response<ConnectToGameResponse>> {
        return m_Service.connectToGame(m_UserData.authToken,
            ConnectToGameRequest(gameName, m_UserData.userName, m_UserData.userId.toString() , m_GameData.gameId.toString())
        )
    }

    fun resetGameData() {
        m_GameData.reset()
    }
}

