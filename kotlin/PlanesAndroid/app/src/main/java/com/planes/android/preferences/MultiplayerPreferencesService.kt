package com.planes.android.preferences

import android.content.Context
import android.os.Bundle

class MultiplayerPreferencesService internal constructor(private val m_Context: Context): IMultiplayerPreferencesService {
    override var username: String = ""
    override var password: String = ""

    override fun readPreferences() {
        val sp_credentials = m_Context.getSharedPreferences("credentials",
            Context.MODE_PRIVATE)
        username = sp_credentials.getString("username", "").toString()
        password = sp_credentials.getString("password", "").toString()
    }

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        username = savedInstanceState.getString("credentials/username").toString()
        password = savedInstanceState.getString("credentials/password").toString()
    }

    override fun writePreferences() {
        val sp_credentials = m_Context.getSharedPreferences("credentials",
            Context.MODE_PRIVATE).edit()
        sp_credentials.putString("username", username)
        sp_credentials.putString("password", password)
        sp_credentials.commit()
    }

    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString("credentials/username", username)
        savedInstanceState.putString("credentials/password", password)
    }
}