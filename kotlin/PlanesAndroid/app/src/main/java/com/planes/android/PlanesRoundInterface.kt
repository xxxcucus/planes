package com.planes.android

//TODO: to move in common
interface PlanesRoundInterface {
    //creates the PlaneRound object in the game engine
    //must be called a single time
    fun createPlanesRound()

    //show the planes
    fun getRowNo(): Int
    fun getColNo(): Int
    fun getPlaneNo(): Int
    fun getPlaneSquareType(i: Int, j: Int, isComputer: Int): Int

    //edit the board
    fun movePlaneLeft(idx: Int): Int
    fun movePlaneRight(idx: Int): Int
    fun movePlaneUpwards(idx: Int): Int
    fun movePlaneDownwards(idx: Int): Int
    fun rotatePlane(idx: Int): Int
    fun doneClicked()

    //play the game
    fun playerGuessAlreadyMade(row: Int, col: Int): Int
    fun playerGuess(row: Int, col: Int)
    fun playerGuess_RoundEnds(): Boolean
    fun playerGuess_IsPlayerWinner(): Boolean
    fun playerGuess_IsDraw(): Boolean
    fun playerGuess_ComputerMoveGenerated(): Boolean
    fun playerGuess_StatNoPlayerMoves(): Int
    fun playerGuess_StatNoPlayerHits(): Int
    fun playerGuess_StatNoPlayerMisses(): Int
    fun playerGuess_StatNoPlayerDead(): Int
    fun playerGuess_StatNoPlayerWins(): Int
    fun playerGuess_StatNoComputerMoves(): Int
    fun playerGuess_StatNoComputerHits(): Int
    fun playerGuess_StatNoComputerMisses(): Int
    fun playerGuess_StatNoComputerDead(): Int
    fun playerGuess_StatNoComputerWins(): Int
    fun playerGuess_StatNoDraws(): Int
    fun roundEnds()
    fun initRound()

    //show the guesses
    fun getPlayerGuessesNo(): Int
    fun getPlayerGuessRow(idx: Int): Int
    fun getPlayerGuessCol(idx: Int): Int
    fun getPlayerGuessType(idx: Int): Int
    fun getComputerGuessesNo(): Int
    fun getComputerGuessRow(idx: Int): Int
    fun getComputerGuessCol(idx: Int): Int
    fun getComputerGuessType(idx: Int): Int
    fun getGameStage(): Int

    //game options
    fun setComputerSkill(skill: Int): Boolean
    fun setShowPlaneAfterKill(show: Boolean): Boolean
    fun getComputerSkill(): Int
    fun getShowPlaneAfterKill(): Boolean

}