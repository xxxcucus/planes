package com.planes.android.preferences

import android.content.Context
import android.os.Bundle

class MainPreferencesServiceGlobal : IMainPreferencesService {

    override var multiplayerVersion: Boolean
        get() = global_Service!!.multiplayerVersion
        set(version) {
            global_Service!!.multiplayerVersion = version
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
        private var global_Service: MainPreferencesService? = null
    }

    fun createPreferencesService(context: Context) {
        if (global_Service != null) return
        global_Service = MainPreferencesService(context)
    }


}