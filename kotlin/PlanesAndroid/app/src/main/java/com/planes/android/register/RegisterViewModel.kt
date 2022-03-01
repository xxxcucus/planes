package com.planes.android.register

import androidx.lifecycle.ViewModel

class RegisterViewModel(var username: String, var password: String) : ViewModel() {

    var m_Username: String
    var m_Password: String

    init {
        m_Username = username
        m_Password = password
    }
}