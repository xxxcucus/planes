package com.planes.android.preferences

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PreferencesViewModel (): ViewModel() {

    private val m_ComputerSkill = mutableStateOf(2)
    private val m_ShowPlaneAfterKill = mutableStateOf(false)
    private val m_UserName = mutableStateOf<String?>(null)
    private val m_Password = mutableStateOf<String?>(null)

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

    fun getUserName(): String? {
        return m_UserName.value
    }

    fun setUserName(value: String?) {
        m_UserName.value = value
    }

    fun getPassword(): String? {
        return m_Password.value
    }

    fun setPassword(value: String?) {
        m_Password.value = value
    }

}