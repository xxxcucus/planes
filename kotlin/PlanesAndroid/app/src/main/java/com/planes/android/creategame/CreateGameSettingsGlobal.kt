package com.planes.android.creategame

import android.content.Context
import android.os.Bundle
import com.planes.android.preferences.MainPreferencesService

class CreateGameSettingsGlobal: ICreateGameSettings {

    override var createGameState: CreateGameStates
        get() = global_Service!!.createGameState
        set(status) {
            global_Service!!.createGameState = status
        }

    override var gameName: String
        get() = global_Service!!.gameName
    set(name) {
        global_Service!!.gameName = name
    }

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        global_Service!!.readFromSavedInstanceState(savedInstanceState)
    }
    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        global_Service!!.writeToSavedInstanceState(savedInstanceState)
    }

    companion object {
        private var global_Service: CreateGameSettings? = null
    }

    fun createPreferencesService(context: Context) {
        if (global_Service != null) return
        global_Service = CreateGameSettings(context)
    }
}