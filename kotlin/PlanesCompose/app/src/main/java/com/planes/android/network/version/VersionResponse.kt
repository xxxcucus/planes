package com.planes.android.network.version

import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.SerializedName


data class VersionResponse (
    @SerializedName("versionString")
    var m_VersionString: String
)