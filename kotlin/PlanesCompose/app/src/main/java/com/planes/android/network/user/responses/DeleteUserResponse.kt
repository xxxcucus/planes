package com.planes.android.network.user.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class DeleteUserResponse (
    @SerializedName("deactivated")
    var m_Deactivated: Boolean
)