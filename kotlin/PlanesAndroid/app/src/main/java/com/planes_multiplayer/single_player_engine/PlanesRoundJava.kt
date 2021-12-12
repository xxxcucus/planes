package com.planes_multiplayer.single_player_engine

import com.planes.android.PlanesRoundInterface

class PlanesRoundJava : PlanesRoundInterface {
    //creates the PlaneRound object in the game engine
    //must be called a single time
    override fun createPlanesRound() {
        if (global_Round != null) return
        global_Round = PlaneRound(10, 10, 3)
        global_Round!!.initRound()
    }

    //show the planes
    override fun getRowNo(): Int {
        return global_Round!!.getRowNo()
    }

    override fun getColNo(): Int {
        return global_Round!!.getColNo()
    }

    override fun getPlaneNo(): Int {
        return global_Round!!.getPlaneNo()
    }

    override fun getPlaneSquareType(i: Int, j: Int, isComputer: Int): Int {
        return global_Round!!.getPlaneSquareType(i, j, if (isComputer > 0) true else false)
    }

    //edit the board
    override fun movePlaneLeft(idx: Int): Int {
        return if (global_Round!!.movePlaneLeft(idx)) 1 else 0
    }

    override fun movePlaneRight(idx: Int): Int {
        return if (global_Round!!.movePlaneRight(idx)) 1 else 0
    }

    override fun movePlaneUpwards(idx: Int): Int {
        return if (global_Round!!.movePlaneUpwards(idx)) 1 else 0
    }

    override fun movePlaneDownwards(idx: Int): Int {
        return if (global_Round!!.movePlaneDownwards(idx)) 1 else 0
    }

    override fun rotatePlane(idx: Int): Int {
        return if (global_Round!!.rotatePlane(idx)) 1 else 0
    }

    override fun doneClicked() {
        global_Round!!.doneEditing()
    }

    //play the game
    override fun playerGuessAlreadyMade(row: Int, col: Int): Int {
        return global_Round!!.playerGuessAlreadyMade(row, col)
    }

    override fun playerGuess(row: Int, col: Int) {
        val result = global_Round!!.playerGuessIncomplete(row, col)
        global_Guess_Result = result.first
        global_Player_Guess_Reaction = result.second
    }

    override fun playerGuess_RoundEnds(): Boolean {
        return global_Player_Guess_Reaction.m_RoundEnds
    }

    override fun playerGuess_IsDraw(): Boolean {
        return global_Player_Guess_Reaction.m_IsDraw
    }

    override fun playerGuess_IsPlayerWinner(): Boolean {
        return global_Player_Guess_Reaction.m_isPlayerWinner
    }

    override fun playerGuess_ComputerMoveGenerated(): Boolean {
        return global_Player_Guess_Reaction.m_ComputerMoveGenerated
    }

    override fun playerGuess_StatNoPlayerMoves(): Int {
        return global_Player_Guess_Reaction.m_GameStats.playerMoves()
    }

    override fun playerGuess_StatNoPlayerHits(): Int {
        return global_Player_Guess_Reaction.m_GameStats.playerHits()
    }

    override fun playerGuess_StatNoPlayerMisses(): Int {
        return global_Player_Guess_Reaction.m_GameStats.playerMisses()
    }

    override fun playerGuess_StatNoPlayerDead(): Int {
        return global_Player_Guess_Reaction.m_GameStats.playerDead()
    }

    override fun playerGuess_StatNoPlayerWins(): Int {
        return global_Player_Guess_Reaction.m_GameStats.playerWins()
    }

    override fun playerGuess_StatNoComputerMoves(): Int {
        return global_Player_Guess_Reaction.m_GameStats.computerMoves()
    }

    override fun playerGuess_StatNoComputerHits(): Int {
        return global_Player_Guess_Reaction.m_GameStats.computerHits()
    }

    override fun playerGuess_StatNoComputerMisses(): Int {
        return global_Player_Guess_Reaction.m_GameStats.computerMisses()
    }

    override fun playerGuess_StatNoComputerDead(): Int {
        return global_Player_Guess_Reaction.m_GameStats.computerDead()
    }

    override fun playerGuess_StatNoComputerWins(): Int {
        return global_Player_Guess_Reaction.m_GameStats.computerWins()
    }

    override fun playerGuess_StatNoDraws(): Int {
        return global_Player_Guess_Reaction.m_GameStats.draws()
    }

    override fun roundEnds() {
        global_Round!!.setRoundEnd()
    }

    override fun initRound() {
        global_Round!!.initRound()
        global_Player_Guess_Reaction.m_GameStats.reset()
    }

    //show the guesses
    override fun getPlayerGuessesNo(): Int {
        return global_Round!!.getPlayerGuessesNo()
    }

    override fun getPlayerGuessRow(idx: Int): Int {
        return global_Round!!.getPlayerGuess(idx).row()
    }

    override fun getPlayerGuessCol(idx: Int): Int {
        return global_Round!!.getPlayerGuess(idx).col()
    }

    override fun getPlayerGuessType(idx: Int): Int {
        return global_Round!!.getPlayerGuess(idx).type().value
    }

    override fun getComputerGuessesNo(): Int {
        return global_Round!!.getComputerGuessesNo()
    }

    override fun getComputerGuessRow(idx: Int): Int {
        return global_Round!!.getComputerGuess(idx).row()
    }

    override fun getComputerGuessCol(idx: Int): Int {
        return global_Round!!.getComputerGuess(idx).col()
    }

    override fun getComputerGuessType(idx: Int): Int {
        return global_Round!!.getComputerGuess(idx).type().value
    }

    override fun getGameStage(): Int {
        return global_Round!!.getCurrentStage()
    }

    override fun setComputerSkill(skill: Int): Boolean {
        return global_Round!!.setComputerSkill(skill)
    }

    override fun setShowPlaneAfterKill(show: Boolean): Boolean {
        return global_Round!!.setShowPlaneAfterKill(show)
    }

    override fun getComputerSkill(): Int {
        return global_Round!!.getComputerSkill()
    }

    override fun getShowPlaneAfterKill(): Boolean {
        return global_Round!!.getShowPlaneAfterKill()
    }

    companion object {
        private var global_Round: PlaneRound? = null
        private var global_Guess_Result = Type.Miss
        private var global_Player_Guess_Reaction = PlayerGuessReaction()
    }
}