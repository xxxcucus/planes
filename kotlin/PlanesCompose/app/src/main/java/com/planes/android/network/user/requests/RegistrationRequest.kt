package com.planes.android.network.user.requests

import com.google.gson.annotations.SerializedName


data class RegistrationRequest(
    @SerializedName("username")
    var m_Username: String,

    @SerializedName("password")
    var m_Password: String)