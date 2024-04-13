package com.planes.android.deleteuser

import android.content.Context
import androidx.lifecycle.ViewModel
import com.planes.android.R
import java.lang.ref.WeakReference

class DeleteUserViewModel(var username: String, context: Context) : ViewModel() {

    var m_Username: String
    var m_LoginStatus: String
    var m_Context: WeakReference<Context>

    init {
        m_Context = WeakReference(context)
        m_Username = username
        m_LoginStatus = if (m_Username.isEmpty())
            m_Context.get()!!.resources.getString(R.string.nouser)
        else
            m_Context.get()!!.resources.getString(R.string.userloggedin)
    }
}