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
        m_LoginStatus = if (m_Username.isEmpty())
            m_Context.resources.getString(R.string.nouser)
        else
            m_Context.resources.getString(R.string.userloggedin)
    }
}