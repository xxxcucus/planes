package com.planes.android.network.user.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class LogoutResponse (
    @SerializedName("loggedOut")
    var m_LoggedOut: Boolean
)