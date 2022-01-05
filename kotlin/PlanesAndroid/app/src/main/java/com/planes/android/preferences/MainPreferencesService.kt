package com.planes.android.preferences

import android.content.Context
import android.os.Bundle

class MainPreferencesService internal constructor(private val m_Context: Context): IMainPreferencesService {

    override var multiplayerVersion: Boolean = false

    override fun readPreferences() {
        val sp_multiplayer = m_Context.getSharedPreferences("multiplayer", Context.MODE_PRIVATE)
        multiplayerVersion = sp_multiplayer.getBoolean("multiplayerversion", false)
    }

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        multiplayerVersion = savedInstanceState.getBoolean("multiplayer/multiplayerversion")
    }

    override fun writePreferences() {
        val sp_multiplayer = m_Context.getSharedPreferences("multiplayer",
            Context.MODE_PRIVATE).edit()
        sp_multiplayer.putBoolean("multiplayerversion", multiplayerVersion)
        sp_multiplayer.commit()
    }

    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("multiplayer/multiplayerversion", multiplayerVersion)
    }

}