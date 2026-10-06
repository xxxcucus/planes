package com.planes.android.network.version

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class VersionResponse (
    @SerializedName("versionString")
    var m_VersionString: String
)