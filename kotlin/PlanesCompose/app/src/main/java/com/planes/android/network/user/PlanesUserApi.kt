package com.planes.android.network.user

import com.planes.android.network.user.requests.DeleteUserRequest
import com.planes.android.network.user.requests.LoginRequest
import com.planes.android.network.user.requests.LogoutRequest
import com.planes.android.network.user.requests.NoRobotRequest
import com.planes.android.network.user.requests.RegistrationRequest
import com.planes.android.network.user.responses.DeleteUserResponse
import com.planes.android.network.user.responses.LoginResponse
import com.planes.android.network.user.responses.LogoutResponse
import com.planes.android.network.user.responses.NoRobotResponse
import com.planes.android.network.user.responses.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface PlanesUserApi {

    @POST("login/")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body user: LoginRequest): Response<LoginResponse>

    @POST("operations/logout")
    @Headers("Content-Type: application/json")
    suspend fun logout(
        @Header("Authorization") authorization: String,
        @Body user: LogoutRequest
    ): Response<LogoutResponse>

    @POST("users/deactivate_user")
    @Headers("Content-Type: application/json")
    suspend fun deactivateUser(
        @Header("Authorization") authorization: String,
        @Body user: DeleteUserRequest
    ): Response<DeleteUserResponse>

    @POST("users/registration_request")
    @Headers("Content-Type: application/json")
    suspend fun register(@Body user: RegistrationRequest): Response<RegistrationResponse>

    @POST("users/registration_confirm")
    @Headers("Content-Type: application/json")
    suspend fun norobot(@Body user: NoRobotRequest): Response<NoRobotResponse>
}
