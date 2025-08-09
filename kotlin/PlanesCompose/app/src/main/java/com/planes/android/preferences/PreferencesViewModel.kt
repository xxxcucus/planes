package com.planes.android.preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

    private val _ComputerSkill = MutableStateFlow(2)
    val m_ComputerSkill: StateFlow<Int> = _ComputerSkill

    private val _ShowPlaneAfterKill = MutableStateFlow(false)
    val m_ShowPlaneAfterKill: StateFlow<Boolean> = _ShowPlaneAfterKill

    private val _UserName = MutableStateFlow("")
    val m_UserName : StateFlow<String> = _UserName

    private val _Password = MutableStateFlow("")
    val m_Password : StateFlow<String> = _Password

    @Composable
    fun getComputerSkill(): Int {
        return m_ComputerSkill.collectAsState().value
    }
    fun setComputerSkill(value: Int) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[COMPUTER_SKILL] = value
            }
        }
    }

    @Composable
    fun getShowPlaneAfterKill(): Boolean {
        return m_ShowPlaneAfterKill.collectAsState().value
    }
    fun setShowPlaneAfterKill(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[SHOW_PLANE] = value
            }
        }
    }

    @Composable
    fun getUserName(): String {
        return m_UserName.collectAsState().value
    }

    fun setUserName(value: String) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[USERNAME] = value
            }
        }
    }

    @Composable
    fun getPassword(): String {
        return m_Password.collectAsState().value
    }

    fun setPassword(value: String) {
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
                _ComputerSkill.value = userprefs.computerSkill
                _ShowPlaneAfterKill.value = userprefs.showPlaneAfterKill
                _UserName.value = userprefs.userName
                _Password.value = userprefs.password
            }
        }
    }
}