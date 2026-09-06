package com.planes.android.network.version

import com.google.gson.annotations.SerializedName


data class VersionResponse (
    @SerializedName("versionString")
    var m_VersionString: String
)