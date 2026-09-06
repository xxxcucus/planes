package com.planes.android.screens.singleplayergame

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.planes.singleplayerengine.SinglePlayerRoundInterface
import com.planes.singleplayerengine.Type
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameStatsViewModelSinglePlayer  @Inject constructor(planeRound: SinglePlayerRoundInterface): ViewModel() {
    var m_playerMoves = mutableIntStateOf(0)
    var m_playerHits = mutableIntStateOf(0)
    var m_playerDead = mutableIntStateOf(0)
    var m_playerMisses = mutableIntStateOf(0)
    var m_computerMoves = mutableIntStateOf(0)
    var m_computerHits = mutableIntStateOf(0)
    var m_computerDead = mutableIntStateOf(0)
    var m_computerMisses = mutableIntStateOf(0)
    var m_lastComputerUpdate = mutableStateOf(Type.Miss)
    var m_lastPlayerUpdate = mutableStateOf(Type.Miss)

    //keeps the score
    var m_playerWins = mutableIntStateOf(0)
    var m_computerWins = mutableIntStateOf(0)
    var m_draws = mutableIntStateOf(0)

    var m_PlaneRound = planeRound

    fun updateFromPlaneRound() {
        m_playerMoves.intValue = m_PlaneRound.playerGuess_StatNoPlayerMoves()
        m_playerHits.intValue = m_PlaneRound.playerGuess_StatNoPlayerHits()
        m_playerDead.intValue = m_PlaneRound.playerGuess_StatNoPlayerDead()
        m_playerMisses.intValue = m_PlaneRound.playerGuess_StatNoPlayerMisses()

        m_computerMoves.intValue = m_PlaneRound.playerGuess_StatNoComputerMoves()
        m_computerHits.intValue = m_PlaneRound.playerGuess_StatNoComputerHits()
        m_computerDead.intValue = m_PlaneRound.playerGuess_StatNoComputerDead()
        m_computerMisses.intValue = m_PlaneRound.playerGuess_StatNoComputerMisses()

        m_playerWins.intValue = m_PlaneRound.playerGuess_StatNoPlayerWins()
        m_computerWins.intValue = m_PlaneRound.playerGuess_StatNoComputerWins()
        m_draws.intValue = m_PlaneRound.playerGuess_StatNoDraws()

        m_lastComputerUpdate.value = m_PlaneRound.playerGuess_StatLastComputeUpdate()
        m_lastPlayerUpdate.value = m_PlaneRound.playerGuess_StatLastPlayerUpdate()
    }

    fun resetRoundStats() {
        setPlayerMoves(0)
        setPlayerHits(0)
        setPlayerDead(0)
        setPlayerMisses(0)

        setComputerMoves(0)
        setComputerHits(0)
        setComputerDead(0)
        setComputerMisses(0)
    }

    fun setPlayerMoves(moves: Int) {
        m_playerMoves.intValue = moves
    }

    fun getPlayerMoves(): Int {
        return m_playerMoves.intValue
    }

    fun setComputerMoves(moves: Int) {
        m_computerMoves.intValue = moves
    }

    fun getComputerMoves(): Int {
        return m_computerMoves.intValue
    }

    fun setPlayerHits(hits: Int) {
        m_playerHits.intValue = hits
    }

    fun getPlayerHits(): Int {
        return m_playerHits.intValue
    }

    fun setComputerHits(hits: Int) {
        m_computerHits.intValue = hits
    }

    fun getComputerHits(): Int {
        return m_computerHits.intValue
    }

    fun setPlayerDead(dead : Int) {
        m_playerDead.intValue = dead
    }

    fun getPlayerDead() : Int {
        return m_playerDead.intValue
    }

    fun setComputerDead(dead: Int) {
        m_computerDead.intValue = dead
    }

    fun getComputerDead() : Int {
        return m_computerDead.intValue
    }

    fun setPlayerMisses(misses: Int) {
        m_playerMisses.intValue = misses
    }

    fun getPlayerMisses(): Int {
        return m_playerMisses.intValue
    }

    fun setComputerMisses(misses: Int) {
        m_computerMisses.intValue = misses
    }

    fun getComputerMisses(): Int {
        return m_computerMisses.intValue
    }

    fun setPlayerWins(wins: Int) {
        m_playerWins.intValue = wins
    }

    fun getPlayerWins(): Int {
        return m_playerWins.intValue
    }

    fun setComputerWins(wins: Int) {
        m_computerWins.intValue = wins
    }

    fun getComputerWins() : Int {
        return m_computerWins.intValue
    }

    fun setDraws(draws: Int) {
        m_draws.intValue = draws
    }
    
    fun getDraws() : Int {
        return m_draws.intValue
    }

    fun getLastComputerMove(): Type {
        return m_lastComputerUpdate.value
    }

    fun getLastPlayerMove(): Type {
        return m_lastPlayerUpdate.value
    }
}