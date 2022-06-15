package com.planes.single_player_engine

enum class GameStages(val value: Int) {
    GameNotStarted(0), BoardEditing(1), Game(2), WaitForOpponentPlanesPositions(3), WaitForOpponentMoves(4);
}