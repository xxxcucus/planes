package com.planes.android.preferences

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PreferencesViewModel (private val computerSkill: Int = 2,
                            private val showPlaneAfterKill: Boolean = false,
                            private val username: String? = null,
                            private val password: String? = null): ViewModel() {

    private val m_ComputerSkill = mutableStateOf(computerSkill)
    private val m_ShowPlaneAfterKill = mutableStateOf(showPlaneAfterKill)
    private val m_UserName = mutableStateOf(username)
    private val m_Password = mutableStateOf(password)

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