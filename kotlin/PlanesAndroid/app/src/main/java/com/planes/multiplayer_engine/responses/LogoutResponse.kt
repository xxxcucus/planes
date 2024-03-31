package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

class LogoutResponse (
    @SerializedName("loggedOut")
    var m_LoggedOut: Boolean
)
