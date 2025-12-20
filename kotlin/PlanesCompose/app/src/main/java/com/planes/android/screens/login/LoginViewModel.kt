package com.planes.android.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.network.user.requests.LoginRequest
import com.planes.android.repository.PlanesUserRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: PlanesUserRepository): ViewModel() {

    private var m_UserName = mutableStateOf("")
    private var m_Password = mutableStateOf("")
    private var m_Loading = mutableStateOf(false)
    private var m_LoggedInUserId = mutableStateOf<String?>(null)
    private var m_LoggedInUsername = mutableStateOf<String?>(null)
    private var m_LoggedInToken = mutableStateOf<String?>(null)
    private var m_Error = mutableStateOf<String?>(null)

    fun getUserName(): String {
        return m_UserName.value
    }

    fun setUserName(value: String) {
        m_UserName.value = value
    }

    fun getPassword(): String {
        return m_Password.value
    }

    fun setPassword(value: String) {
        m_Password.value = value
    }

    fun getLoading(): Boolean {
        return m_Loading.value
    }

    fun setLoading(value: Boolean) {
        m_Loading.value = value
    }

    fun getLoggedInUserId(): String? {
        return m_LoggedInUserId.value
    }

    fun setLoggedInUserId(value: String?) {
        m_LoggedInUserId.value = value
    }

    fun getLoggedInUsername(): String? {
        return m_LoggedInUsername.value
    }

    fun setLoggedInUsername(value: String?) {
        m_LoggedInUsername.value = value
    }

    fun getLoggedInToken() : String? {
        return m_LoggedInToken.value
    }

    fun setLoggedInToken(value: String?) {
        m_LoggedInToken.value = value
    }

    fun getError(): String? {
        return m_Error.value
    }

    fun setError(value: String?) {
        m_Error.value = value
    }

    fun login() {
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

                Log.d("PlanesCompose", "Login successfull with id ${getLoggedInUserId()}, username ${getLoggedInUsername()} and token ${getLoggedInToken()}")
            }
            m_Loading.value = result.loading!!
        }
    }
}