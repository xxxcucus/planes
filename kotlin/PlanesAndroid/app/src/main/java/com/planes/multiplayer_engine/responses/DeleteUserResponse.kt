package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

class DeleteUserResponse (
    @SerializedName("deactivated")
    var m_Deactivated: Boolean
)