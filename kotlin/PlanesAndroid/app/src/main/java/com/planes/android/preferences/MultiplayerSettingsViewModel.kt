package com.planes.android.preferences

import androidx.lifecycle.ViewModel

class MultiplayerSettingsViewModel(var username: String, var password: String, var multiplayerVersion: Boolean) : ViewModel() {

    var m_Username: String = username
    var m_Password: String = password
    var m_MultiplayerVersion: Boolean = multiplayerVersion
}