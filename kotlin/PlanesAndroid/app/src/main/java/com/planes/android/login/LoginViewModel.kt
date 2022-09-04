package com.planes.android.login

import androidx.lifecycle.ViewModel

class LoginViewModel(var username: String, var password: String) : ViewModel() {

    var m_Username: String = username
    var m_Password: String = password

}