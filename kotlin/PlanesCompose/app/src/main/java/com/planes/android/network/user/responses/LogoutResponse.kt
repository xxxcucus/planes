package com.planes.android.network.user.responses

import com.google.gson.annotations.SerializedName

class LogoutResponse (
    @SerializedName("loggedOut")
    var m_LoggedOut: Boolean
)