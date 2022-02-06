package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName

data class VersionResponse (
    @SerializedName("versionString")
    var m_VersionString: String
)