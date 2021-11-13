package com.planes_multiplayer.android

import androidx.lifecycle.ViewModel

class SettingsViewModel(computerSkill: Int, var showPlaneAfterKill: Boolean) : ViewModel() {

    var m_ComputerSkill: Int = 0
    var m_ShowPlaneAfterKill: Boolean = false

    init {
        m_ComputerSkill = computerSkill
        m_ShowPlaneAfterKill = showPlaneAfterKill
    }
}