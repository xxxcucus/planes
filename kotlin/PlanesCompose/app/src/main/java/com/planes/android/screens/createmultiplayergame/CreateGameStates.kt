package com.planes.android.screens.createmultiplayergame

enum class CreateGameStates {
    StatusNotRequested, StatusRequested, StatusReceived,
    GameCreationRequested,  GameCreationComplete, ConnectedToGameRequested, ConnectedComplete,
    PollingForConnectionStarted, PollingForConnectionEnded
}