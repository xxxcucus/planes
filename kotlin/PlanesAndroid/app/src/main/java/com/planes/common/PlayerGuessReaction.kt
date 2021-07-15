package com.planes.common

import com.planes.common.GuessPoint
import com.planes.common.GameStatistics

class PlayerGuessReaction {
    //victory for computer or player
    var m_RoundEnds = false

    //is it a draw
    var m_IsDraw = false

    //who won
    var m_isPlayerWinner = false

    //if no victory then a computer move is generated
    var m_ComputerMoveGenerated = false

    //which computer move was generated
    var m_ComputerGuess: GuessPoint

    var m_GameStats: GameStatistics

    init {
        m_ComputerGuess = GuessPoint(0, 0)
        m_GameStats = GameStatistics()
    }
}