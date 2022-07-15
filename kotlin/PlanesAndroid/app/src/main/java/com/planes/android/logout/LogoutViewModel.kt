package com.planes.android.logout

import android.content.Context
import androidx.lifecycle.ViewModel
import com.planes.android.R

class LogoutViewModel(var username: String, context: Context) : ViewModel() {

    var m_Username: String
    var m_LoginStatus: String
    var m_Context: Context

    init {
        m_Context = context
        m_Username = username
        if (m_Username.isNullOrEmpty())
            m_LoginStatus = m_Context.resources.getString(R.string.nouser)
        else
            m_LoginStatus = m_Context.resources.getString(R.string.userloggedin)
    }
}