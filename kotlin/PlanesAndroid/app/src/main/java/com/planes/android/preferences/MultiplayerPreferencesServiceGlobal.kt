package com.planes.android.preferences

import android.content.Context
import android.os.Bundle

class MultiplayerPreferencesServiceGlobal: IMultiplayerPreferencesService {

    override var username: String
        get() = global_Service!!.username
        set(username) {
            global_Service!!.username = username
        }

    override var password: String
        get() = global_Service!!.password
        set(password) {
            global_Service!!.password = password
        }

    override fun readPreferences() {
        global_Service!!.readPreferences()
    }
    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        global_Service!!.readFromSavedInstanceState(savedInstanceState)
    }
    override fun writePreferences() {
        global_Service!!.writePreferences()
    }
    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        global_Service!!.writeToSavedInstanceState(savedInstanceState)
    }

    companion object {
        private var global_Service: MultiplayerPreferencesService? = null
    }

    fun createPreferencesService(context: Context) {
        if (global_Service != null) return
        global_Service = MultiplayerPreferencesService(context)
    }

}