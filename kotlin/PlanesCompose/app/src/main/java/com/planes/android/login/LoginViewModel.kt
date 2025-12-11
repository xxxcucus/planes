package com.planes.android.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import dagger.hilt.android.lifecycle.HiltViewModel



class LoginViewModel: ViewModel() {

    private var m_UserName = mutableStateOf("")
    private var m_Password = mutableStateOf("")

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
}