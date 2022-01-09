package com.planes.android.preferences

import androidx.lifecycle.ViewModel

class SinglePlayerSettingsViewModel(var computerSkill: Int, var showPlaneAfterKill: Boolean, var multiplayerVersion: Boolean) : ViewModel() {

    var m_ComputerSkill: Int = 0
    var m_ShowPlaneAfterKill: Boolean = false
    var m_MultiplayerVersion: Boolean = false

    init {
        m_ComputerSkill = computerSkill
        m_ShowPlaneAfterKill = showPlaneAfterKill
        m_MultiplayerVersion = multiplayerVersion
    }
}