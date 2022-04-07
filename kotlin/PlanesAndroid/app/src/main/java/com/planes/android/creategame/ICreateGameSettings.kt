package com.planes.android.creategame

import com.planes.android.register.INotPersistingPreferencesService

interface ICreateGameSettings: INotPersistingPreferencesService {
    var createGameState: CreateGameStates
    var gameName: String
}