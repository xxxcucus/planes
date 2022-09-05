package com.planes.single_player_engine

enum class GameStages(val value: Int) {
    GameNotStarted(0), BoardEditing(1), Game(2), WaitForOpponentPlanesPositions(3), WaitForOpponentMoves(4),
    SendRemainingMoves(5);

    companion object {
        private val map = values().associateBy(GameStages::value)
        operator fun get(value: Int) = map[value]
    }

}