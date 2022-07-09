package com.planes.android.game.multiplayer

import com.planes.single_player_engine.GuessPoint

interface IGameFragmentMultiplayer {

    fun sendWinner(draw: Boolean, winnerId: Long)
    fun sendMove(gp: GuessPoint, playerMoveIndex: Int)
    fun pollForOpponentMoves(playerFinished: Boolean)
}