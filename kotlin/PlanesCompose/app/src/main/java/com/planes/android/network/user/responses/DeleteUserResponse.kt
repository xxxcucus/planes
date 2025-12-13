package com.planes.android.network.user.responses

import com.google.gson.annotations.SerializedName

class DeleteUserResponse (
    @SerializedName("deactivated")
    var m_Deactivated: Boolean
)