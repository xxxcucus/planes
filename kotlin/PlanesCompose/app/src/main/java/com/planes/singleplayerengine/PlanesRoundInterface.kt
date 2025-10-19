package com.planes.singleplayerengine

import androidx.core.util.Pair
import java.util.Vector

//TODO: to move in common
interface PlanesRoundInterface {
    //creates the PlaneRound object in the game engine
    //must be called a single time
    //fun createPlanesRound()

    //show the planes
    fun getRowNo(): Int
    fun getColNo(): Int
    fun getPlaneNo(): Int
    fun getPlaneSquareType(row: Int, col: Int, isComputer: Boolean): Int

    fun getPlayerPlanes(): Vector<Plane>
    fun getComputerPlanes(): Vector<Plane>
    fun setPlayerPlanes(planes: Vector<Plane>)

    //edit the board
    fun movePlaneLeft(idx: Int): Boolean
    fun movePlaneRight(idx: Int): Boolean
    fun movePlaneUpwards(idx: Int): Boolean
    fun movePlaneDownwards(idx: Int): Boolean
    fun rotatePlane(idx: Int): Boolean
    fun doneClicked()

    //play the game
    fun playerGuessAlreadyMade(row: Int, col: Int): Boolean
    fun playerGuess(row: Int, col: Int)
    fun playerGuess(gp: GuessPoint): PlayerGuessReaction
    fun playerGuessIncomplete(row: Int, col: Int): Pair<Type, PlayerGuessReaction>
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
    fun playerGuess_GuessResult(): Type
    fun roundEnds(isComputerWinner: Boolean, isDraw : Boolean)
    fun initRound()

    fun cancelRound()

    //show the guesses
    fun getPlayerGuessesNo(): Int
    fun getPlayerGuessRow(idx: Int): Int
    fun getPlayerGuessCol(idx: Int): Int
    fun getPlayerGuessType(idx: Int): Type
    fun getComputerGuessesNo(): Int
    fun getComputerGuessRow(idx: Int): Int
    fun getComputerGuessCol(idx: Int): Int
    fun getComputerGuessType(idx: Int): Type
    fun getLastComputerGuess(): GuessPoint?
    fun getGameStage(): GameStages

    //game options
    fun setComputerSkill(skill: Int): Boolean
    fun setShowPlaneAfterKill(show: Boolean): Boolean
    fun getComputerSkill(): Int
    fun getShowPlaneAfterKill(): Boolean

    fun getRoundEndStatus(): Int


}