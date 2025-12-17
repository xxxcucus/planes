package com.planes.android.screens.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.planes.android.repository.PlanesUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: PlanesUserRepository): ViewModel() {

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