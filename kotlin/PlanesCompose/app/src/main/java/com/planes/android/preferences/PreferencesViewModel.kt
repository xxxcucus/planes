package com.planes.android.preferences


import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
): ViewModel() {

    private companion object {
        val COMPUTER_SKILL = intPreferencesKey("computer_skill")
        val SHOW_PLANE = booleanPreferencesKey("show_plane")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
    }

    private var m_ComputerSkill = mutableStateOf(2)
    private var m_ShowPlaneAfterKill = mutableStateOf(false)
    private var m_UserName = mutableStateOf("")
    private var m_Password = mutableStateOf("")

    fun getComputerSkill(): Int {
        return m_ComputerSkill.value
    }

    fun setComputerSkill(value: Int) {
        m_ComputerSkill.value = value

        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[COMPUTER_SKILL] = value
            }
        }
    }

    fun getShowPlaneAfterKill() : Boolean {
        return m_ShowPlaneAfterKill.value
    }

    fun setShowPlaneAfterKill(value: Boolean) {
        m_ShowPlaneAfterKill.value = value

        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[SHOW_PLANE] = value
            }
        }
    }

    fun getUserName(): String {
        return m_UserName.value
    }

    fun setUserName(value: String) {
        m_UserName.value = value

        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[USERNAME] = value
            }
        }
    }

    fun getPassword(): String {
        return m_Password.value
    }

    fun setPassword(value: String) {
        m_Password.value = value

        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[PASSWORD] = value
            }
        }
    }


    init {
        updateFields()
    }

    private fun updateFields() {
        viewModelScope.launch {
            dataStore.data.map { prefs ->
                UserPreferences(
                    computerSkill = prefs[COMPUTER_SKILL] ?: 2,
                    showPlaneAfterKill = prefs[SHOW_PLANE] ?: false,
                    userName = prefs[USERNAME] ?: "",
                    password = prefs[PASSWORD] ?: "")

            }.collect { userprefs ->
                m_ComputerSkill.value = userprefs.computerSkill
                m_ShowPlaneAfterKill.value = userprefs.showPlaneAfterKill
                m_UserName.value = userprefs.userName
                m_Password.value = userprefs.password
            }

        }
    }

    fun savePreferences() {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[COMPUTER_SKILL] = m_ComputerSkill.value
                prefs[SHOW_PLANE] = m_ShowPlaneAfterKill.value
                prefs[USERNAME] = m_UserName.value
                prefs[PASSWORD] = m_Password.value
            }
        }
    }

}