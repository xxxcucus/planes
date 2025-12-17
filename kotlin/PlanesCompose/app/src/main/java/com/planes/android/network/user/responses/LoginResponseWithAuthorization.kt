package com.planes.android.network.user.responses

import android.util.Log
import com.google.gson.annotations.SerializedName

class LoginResponseWithAuthorization(
    @SerializedName("id")
    override var m_Id: String,
    @SerializedName("username")
    override var m_Username: String,
    var m_Authorization: String): LoginResponse(m_Id, m_Username)