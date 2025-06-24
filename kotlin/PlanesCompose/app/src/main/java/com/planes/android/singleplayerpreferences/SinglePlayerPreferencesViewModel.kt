package com.planes.android.singleplayerpreferences

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SinglePlayerPreferencesViewModel (private val computerSkill: Int = 2,
                                       private val showPlaneAfterKill: Boolean = false): ViewModel() {

    private val m_ComputerSkill = mutableStateOf(computerSkill)
    private val m_ShowPlaneAfterKill = mutableStateOf(showPlaneAfterKill)

    fun getComputerSkill(): Int {
        return m_ComputerSkill.value
    }
    fun setComputerSkill(value: Int) {
        m_ComputerSkill.value = value
    }

    fun getShowPlaneAfterKill(): Boolean {
        return m_ShowPlaneAfterKill.value
    }
    fun setShowPlaneAfterKill(value: Boolean) {
        m_ShowPlaneAfterKill.value = value
    }
}