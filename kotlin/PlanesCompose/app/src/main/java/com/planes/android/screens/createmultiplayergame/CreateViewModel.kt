package com.planes.android.screens.createmultiplayergame

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(private val repository: PlanesGameRepository): ViewModel() {

    private var m_GameName = mutableStateOf("")
    private var m_Loading = mutableStateOf(false)
    private var m_ConnectedGameId = mutableStateOf<String?>(null)
    private var m_ConnectedGameName = mutableStateOf<String?>(null)
    private var m_Error = mutableStateOf<String?>(null)

    fun getGameName(): String {
        return m_GameName.value
    }

    fun setGameName(value: String) {
        m_GameName.value = value
    }

    fun getLoading(): Boolean {
        return m_Loading.value
    }

    fun setLoading(value: Boolean) {
        m_Loading.value = value
    }

    fun getConnectedGameId(): String? {
        return m_ConnectedGameId.value
    }

    fun setConnectedGameId(value: String?) {
        m_ConnectedGameId.value = value
    }

    fun getConnectedGameName(): String? {
        return m_ConnectedGameName.value
    }

    fun setConnectedGameName(value: String?) {
        m_ConnectedGameName.value = value
    }

    fun getError(): String? {
        return m_Error.value
    }

    fun setError(value: String?) {
        m_Error.value = value
    }

    /*fun createGame() {
        viewModelScope.launch {
            m_Loading.value = true
            m_Error.value = null
            val result = withContext(Dispatchers.IO) {
                repository.login(getUserName(), getPassword())
            }

            if (result.data == null) {
                Log.d("PlanesCompose", "Login error ${result.e}")
                m_Error.value = result.e
            } else {
                m_LoggedInUserId.value = result.data?.m_Id
                m_LoggedInUsername.value = result.data?.m_Username
                m_LoggedInToken.value = result.data?.m_Authorization

                Log.d("PlanesCompose", "Login successfull with id ${getLoggedInUserId()}, username ${getLoggedInUserName()} and token ${getLoggedInToken()}")
            }
            m_Loading.value = result.loading!!
        }
    }

    fun isLoggedIn(): Boolean {
        if (m_LoggedInUserId.value == null)
            return false

        if (m_LoggedInUsername.value == null)
            return false

        if (m_LoggedInToken.value == null)
            return false

        return true
    }*/

}