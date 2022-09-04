package com.planes.android.creategame

import android.os.Bundle

class CreateGameSettings internal constructor() :
    ICreateGameSettings {

    override var createGameState = CreateGameStates.NotSubmitted
    override var gameName = ""

    override fun readFromSavedInstanceState(savedInstanceState: Bundle) {
        createGameState = CreateGameStates.valueOf(savedInstanceState.getString("creategame/state")!!)
    }

    override fun writeToSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString("creategame/state", createGameState.toString())
    }
}