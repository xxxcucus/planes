package com.planes.android

import android.content.Context
import android.os.Bundle

class PreferencesServiceGlobal : IPreferencesService {

    override var computerSkill: Int
        get() = global_Service!!.computerSkill
        set(skill) {
            global_Service!!.computerSkill = skill
        }

    override var showPlaneAfterKill: Boolean
        get() = global_Service!!.showPlaneAfterKill
        set(show) {
            global_Service!!.showPlaneAfterKill = show
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
        private var global_Service: PreferencesService? = null
    }

    fun createPreferencesService(context: Context) {
        if (global_Service != null) return
        global_Service = PreferencesService(context)
    }

}