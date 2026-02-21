package com.planes.multiplayerengine

import androidx.core.util.Pair
import com.planes.singleplayerengine.GameStages
import com.planes.singleplayerengine.GuessPoint
import com.planes.singleplayerengine.Plane
import com.planes.singleplayerengine.PlaneRound
import com.planes.singleplayerengine.PlayerGuessReaction
import com.planes.singleplayerengine.RoundEndStatus
import com.planes.singleplayerengine.Type
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.Vector

class MultiplayerRound @AssistedInject constructor(
    private val planeRoundFactory: PlaneRound.Factory,
    @Assisted("rowNo") rowNo: Int,
    @Assisted("colNo") colNo: Int,
    @Assisted("planeNo") planeNo: Int): MultiPlayerRoundInterface {

    val m_PlaneRound = planeRoundFactory.createPlaneRound(rowNo, colNo, planeNo)

    @AssistedFactory
    interface Factory {
        fun createMultiplayerRound(
            @Assisted("rowNo") rowNo: Int,
            @Assisted("colNo") colNo: Int,
            @Assisted("planeNo") planeNo: Int): MultiplayerRound
    }

    override fun getRowNo(): Int {
        return m_PlaneRound.getRowNo()
    }

    override fun getColNo(): Int {
        return m_PlaneRound.getColNo()
    }

    override fun getPlaneNo(): Int {
        return m_PlaneRound.getPlaneNo()
    }

    override fun getPlaneSquareType(row: Int, col: Int, isComputer: Boolean): Int {
        return m_PlaneRound.getPlaneSquareType(row, col, isComputer)
    }

    override fun getPlayerPlanes(): Vector<Plane> {
        return m_PlaneRound.getPlayerPlanes()
    }

    override fun getComputerPlanes(): Vector<Plane> {
        return m_PlaneRound.getComputerPlanes()
    }

    override fun setPlayerPlanes(planes: Vector<Plane>) {
        return m_PlaneRound.setPlayerPlanes(planes)
    }

    override fun setComputerPlanes(planes: Vector<Plane>) {
        return m_PlaneRound.setComputerPlanes(planes)
    }

    override fun movePlaneLeft(idx: Int): Boolean {
        return m_PlaneRound.movePlaneLeft(idx)
    }

    override fun movePlaneRight(idx: Int): Boolean {
        return m_PlaneRound.movePlaneRight(idx)
    }

    override fun movePlaneUpwards(idx: Int): Boolean {
        return m_PlaneRound.movePlaneUpwards(idx)
    }

    override fun movePlaneDownwards(idx: Int): Boolean {
        return m_PlaneRound.movePlaneDownwards(idx)
    }

    override fun rotatePlane(idx: Int): Boolean {
        return m_PlaneRound.rotatePlane(idx)
    }

    override fun doneClicked() {
        return m_PlaneRound.doneClicked()
    }

    override fun playerGuessAlreadyMade(row: Int, col: Int): Boolean {
        return m_PlaneRound.playerGuessAlreadyMade(row, col)
    }

    override fun computerGuessAlreadyMade(row: Int, col: Int): Boolean {
        return m_PlaneRound.computerGuessAlreadyMade(row, col)
    }

    override fun playerGuess(row: Int, col: Int) {
        return m_PlaneRound.playerGuess(row, col)
    }

    override fun playerGuess(gp: GuessPoint): PlayerGuessReaction {
        return m_PlaneRound.playerGuess(gp)
    }

    override fun playerGuessIncomplete(row: Int, col: Int): Pair<Type, PlayerGuessReaction> {
        return m_PlaneRound.playerGuessIncomplete(row, col)
    }

    override fun playerGuess_RoundEnds(): Boolean {
        return m_PlaneRound.playerGuess_RoundEnds()
    }

    override fun playerGuess_IsPlayerWinner(): Boolean {
        return m_PlaneRound.playerGuess_IsPlayerWinner()
    }

    override fun playerGuess_IsDraw(): Boolean {
        return m_PlaneRound.playerGuess_IsDraw()
    }

    override fun playerGuess_ComputerMoveGenerated(): Boolean {
        return m_PlaneRound.playerGuess_ComputerMoveGenerated()
    }

    override fun playerGuess_StatNoPlayerMoves(): Int {
        return m_PlaneRound.playerGuess_StatNoPlayerMoves()
    }

    override fun playerGuess_StatNoPlayerHits(): Int {
        return m_PlaneRound.playerGuess_StatNoPlayerHits()
    }

    override fun playerGuess_StatNoPlayerMisses(): Int {
        return m_PlaneRound.playerGuess_StatNoPlayerMisses()
    }

    override fun playerGuess_StatNoPlayerDead(): Int {
        return m_PlaneRound.playerGuess_StatNoPlayerDead()
    }

    override fun playerGuess_StatNoPlayerWins(): Int {
        return m_PlaneRound.playerGuess_StatNoPlayerDead()
    }

    override fun playerGuess_StatNoComputerMoves(): Int {
        return m_PlaneRound.playerGuess_StatNoComputerMoves()
    }

    override fun playerGuess_StatNoComputerHits(): Int {
        return m_PlaneRound.playerGuess_StatNoComputerHits()
    }

    override fun playerGuess_StatNoComputerMisses(): Int {
        return m_PlaneRound.playerGuess_StatNoComputerMisses()
    }

    override fun playerGuess_StatNoComputerDead(): Int {
        return m_PlaneRound.playerGuess_StatNoComputerDead()
    }

    override fun playerGuess_StatNoComputerWins(): Int {
        return m_PlaneRound.playerGuess_StatNoComputerWins()
    }

    override fun playerGuess_StatNoDraws(): Int {
        return m_PlaneRound.playerGuess_StatNoDraws()
    }

    override fun playerGuess_GuessResult(): Type {
        return m_PlaneRound.playerGuess_GuessResult()
    }

    override fun playerGuess_StatLastComputeUpdate(): Type {
        return m_PlaneRound.playerGuess_StatLastComputeUpdate()
    }

    override fun playerGuess_StatLastPlayerUpdate(): Type {
        return m_PlaneRound.playerGuess_StatLastPlayerUpdate()
    }

    override fun addComputerMove(row: Int, col: Int) {
        m_PlaneRound.addComputerMove(row, col)
    }

    override fun roundEnds(isComputerWinner: Boolean, isDraw: Boolean) {
        return m_PlaneRound.roundEnds(isComputerWinner, isDraw)
    }

    override fun initRound() {
        return m_PlaneRound.initRound()
    }

    override fun cancelRound() {
        return m_PlaneRound.cancelRound()
    }

    override fun getPlayerGuessesNo(): Int {
        return m_PlaneRound.getPlayerGuessesNo()
    }

    override fun getPlayerGuessRow(idx: Int): Int {
        return m_PlaneRound.getPlayerGuessRow(idx)
    }

    override fun getPlayerGuessCol(idx: Int): Int {
        return m_PlaneRound.getPlayerGuessCol(idx)
    }

    override fun getPlayerGuessType(idx: Int): Type {
        return m_PlaneRound.getPlayerGuessType(idx)
    }

    override fun getComputerGuessesNo(): Int {
        return m_PlaneRound.getComputerGuessesNo()
    }

    override fun getComputerGuessRow(idx: Int): Int {
        return m_PlaneRound.getPlayerGuessRow(idx)
    }

    override fun getComputerGuessCol(idx: Int): Int {
        return m_PlaneRound.getPlayerGuessCol(idx)
    }

    override fun getComputerGuessType(idx: Int): Type {
        return m_PlaneRound.getPlayerGuessType(idx)
    }

    override fun getLastComputerGuess(): GuessPoint? {
        return m_PlaneRound.getLastComputerGuess()
    }

    override fun getGameStage(): GameStages {
        return m_PlaneRound.getGameStage()
    }

    override fun getShowPlaneAfterKillGuess(): Vector<GuessPoint> {
        return m_PlaneRound.getShowPlaneAfterKillGuess()
    }

    override fun setComputerSkill(skill: Int): Boolean {
        return m_PlaneRound.setComputerSkill(skill)
    }

    override fun setShowPlaneAfterKill(show: Boolean): Boolean {
        return m_PlaneRound.setShowPlaneAfterKill(show)
    }

    override fun getComputerSkill(): Int {
        return m_PlaneRound.getComputerSkill()
    }

    override fun getShowPlaneAfterKill(): Boolean {
        return m_PlaneRound.getShowPlaneAfterKill()
    }

    override fun getRoundEndStatus(): RoundEndStatus {
        return m_PlaneRound.getRoundEndStatus()
    }

}