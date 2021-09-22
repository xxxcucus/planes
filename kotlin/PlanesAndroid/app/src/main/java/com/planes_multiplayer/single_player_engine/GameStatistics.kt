package com.planes_multiplayer.single_player_engine

class GameStatistics {
    //keeps the number of moves and various guesses in the current round

    var m_playerMoves = 0
    var m_playerHits = 0
    var m_playerDead = 0
    var m_playerMisses = 0
    var m_computerMoves = 0
    var m_computerHits = 0
    var m_computerDead = 0
    var m_computerMisses = 0

    //keeps the score
    var m_playerWins = 0
    var m_computerWins = 0
    var m_draws = 0

    //constructor
    init {
        reset()
        m_computerWins = 0
        m_playerWins = 0
    }
    //resets the fields related to one round of the game
    fun reset() {
        m_playerMoves = 0
        m_playerHits = 0
        m_playerDead = 0
        m_playerMisses = 0
        m_computerMoves = 0
        m_computerHits = 0
        m_computerDead = 0
        m_computerMisses = 0
    }

    fun playerMoves(): Int {
        return m_playerMoves
    }

    fun playerHits(): Int {
        return m_playerHits
    }

    fun playerDead(): Int {
        return m_playerDead
    }

    fun playerMisses(): Int {
        return m_playerMisses
    }

    fun computerMoves(): Int {
        return m_computerMoves
    }

    fun computerHits(): Int {
        return m_computerHits
    }

    fun computerDead(): Int {
        return m_computerDead
    }

    fun computerMisses(): Int {
        return m_computerMisses
    }

    fun playerWins(): Int {
        return m_playerWins
    }

    fun computerWins(): Int {
        return m_computerWins
    }

    fun draws(): Int {
        return m_draws
    }


    //updates the statistics for one round with one guess
    fun updateStats(gp: GuessPoint, isComputer: Boolean) {
        if (isComputer) {
            m_computerMoves++
            if (gp.type() === Type.Dead) m_computerDead++
            if (gp.type() === Type.Hit) m_computerHits++
            if (gp.type() === Type.Miss) m_computerMisses++
        } else {
            m_playerMoves++
            if (gp.type() === Type.Dead) m_playerDead++
            if (gp.type() === Type.Hit) m_playerHits++
            if (gp.type() === Type.Miss) m_playerMisses++
        }
    }

    //adds to the score
    fun updateWins(isComputerWinner: Boolean) {
        if (isComputerWinner) m_computerWins++ else m_playerWins++
    }

    fun addDrawResult() {
        m_draws++
    }
}