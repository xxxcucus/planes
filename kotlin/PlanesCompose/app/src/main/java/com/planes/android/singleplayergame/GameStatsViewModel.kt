package com.planes.android.singleplayergame

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.planes.singleplayerengine.PlanesRoundInterface
import com.planes.singleplayerengine.Type
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameStatsViewModel  @Inject constructor(planeRound: PlanesRoundInterface): ViewModel() {
    var m_playerMoves = mutableStateOf(0)
    var m_playerHits = mutableStateOf(0)
    var m_playerDead = mutableStateOf(0)
    var m_playerMisses = mutableStateOf(0)
    var m_computerMoves = mutableStateOf(0)
    var m_computerHits = mutableStateOf(0)
    var m_computerDead = mutableStateOf(0)
    var m_computerMisses = mutableStateOf(0)
    var m_lastComputerUpdate = mutableStateOf(Type.Miss)
    var m_lastPlayerUpdate = mutableStateOf(Type.Miss)

    //keeps the score
    var m_playerWins = mutableStateOf(0)
    var m_computerWins = mutableStateOf(0)
    var m_draws = mutableStateOf(0)

    var m_PlaneRound = planeRound

    fun updateFromPlaneRound() {
        m_playerMoves.value = m_PlaneRound.playerGuess_StatNoPlayerMoves()
        m_playerHits.value = m_PlaneRound.playerGuess_StatNoPlayerHits()
        m_playerDead.value = m_PlaneRound.playerGuess_StatNoPlayerDead()
        m_playerMisses.value = m_PlaneRound.playerGuess_StatNoPlayerMisses()

        m_computerMoves.value = m_PlaneRound.playerGuess_StatNoComputerMoves()
        m_computerHits.value = m_PlaneRound.playerGuess_StatNoComputerHits()
        m_computerDead.value = m_PlaneRound.playerGuess_StatNoComputerDead()
        m_computerMisses.value = m_PlaneRound.playerGuess_StatNoComputerMisses()

        m_playerWins.value = m_PlaneRound.playerGuess_StatNoPlayerWins()
        m_computerWins.value = m_PlaneRound.playerGuess_StatNoComputerWins()
        m_draws.value = m_PlaneRound.playerGuess_StatNoDraws()

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
        m_playerMoves.value = moves
    }

    fun getPlayerMoves(): Int {
        return m_playerMoves.value
    }

    fun setComputerMoves(moves: Int) {
        m_computerMoves.value = moves
    }

    fun getComputerMoves(): Int {
        return m_computerMoves.value
    }

    fun setPlayerHits(hits: Int) {
        m_playerHits.value = hits
    }

    fun getPlayerHits(): Int {
        return m_playerHits.value
    }

    fun setComputerHits(hits: Int) {
        m_computerHits.value = hits
    }

    fun getComputerHits(): Int {
        return m_computerHits.value
    }

    fun setPlayerDead(dead : Int) {
        m_playerDead.value = dead
    }

    fun getPlayerDead() : Int {
        return m_playerDead.value
    }

    fun setComputerDead(dead: Int) {
        m_computerDead.value = dead
    }

    fun getComputerDead() : Int {
        return m_computerDead.value
    }

    fun setPlayerMisses(misses: Int) {
        m_playerMisses.value = misses
    }

    fun getPlayerMisses(): Int {
        return m_playerMisses.value
    }

    fun setComputerMisses(misses: Int) {
        m_computerMisses.value = misses
    }

    fun getComputerMisses(): Int {
        return m_computerMisses.value
    }

    fun setPlayerWins(wins: Int) {
        m_playerWins.value = wins
    }

    fun getPlayerWins(): Int {
        return m_playerWins.value
    }

    fun setComputerWins(wins: Int) {
        m_computerWins.value = wins
    }

    fun getComputerWins() : Int {
        return m_computerWins.value
    }

    fun setDraws(draws: Int) {
        m_draws.value = draws
    }
    
    fun getDraws() : Int {
        return m_draws.value
    }

    fun getLastComputerMove(): Type {
        return m_lastComputerUpdate.value
    }

    fun getLastPlayerMove(): Type {
        return m_lastPlayerUpdate.value
    }
}