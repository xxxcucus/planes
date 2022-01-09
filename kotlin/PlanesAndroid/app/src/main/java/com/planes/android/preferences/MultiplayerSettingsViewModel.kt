package com.planes.android.preferences

import androidx.lifecycle.ViewModel

class MultiplayerSettingsViewModel(var username: String, var password: String, var multiplayerVersion: Boolean) : ViewModel() {

    var m_Username: String
    var m_Password: String
    var m_MultiplayerVersion: Boolean = false

    init {
        m_Username = username
        m_Password = password
        m_MultiplayerVersion = multiplayerVersion
    }
}