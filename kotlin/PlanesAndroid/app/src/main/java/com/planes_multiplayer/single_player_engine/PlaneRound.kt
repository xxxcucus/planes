package com.planes_multiplayer.single_player_engine

import androidx.core.util.Pair
import java.util.*

class PlaneRound(rowNo: Int, colNo: Int, planeNo: Int) {

    //whether the computer or the player moves first
    private var m_isComputerFirst = false

    //the  game statistics
    private var m_gameStats: GameStatistics

    //the player and computer's grid
    private var m_PlayerGrid: PlaneGrid
    private var m_ComputerGrid: PlaneGrid

    //the list of guesses for computer and player
    private var m_computerGuessList: Vector<GuessPoint>
    private var m_playerGuessList: Vector<GuessPoint>

    //the computer's strategy
    private var m_computerLogic: ComputerLogic
    private var m_State = GameStages.GameNotStarted

    //size of the grid and number of planes
    private var m_rowNo = 10
    private var m_colNo = 10
    private var m_planeNo = 3
    private var m_RoundOptions: PlaneRoundOptions

    init {
        m_rowNo = rowNo
        m_colNo = colNo
        m_planeNo = planeNo

        //builds the plane grid objects
        m_PlayerGrid = PlaneGrid(m_rowNo, m_colNo, m_planeNo, false)
        m_ComputerGrid = PlaneGrid(m_rowNo, m_colNo, m_planeNo, true)

        //builds the computer logic object
        m_computerLogic = ComputerLogic(m_rowNo, m_colNo, m_planeNo)
        m_computerGuessList = Vector()
        m_playerGuessList = Vector()
        m_gameStats = GameStatistics()
        m_RoundOptions = PlaneRoundOptions()
        reset()
        initRound()
    }

    //inits a new round
    fun initRound() {
        m_PlayerGrid.initGrid()
        m_ComputerGrid.initGrid()
        m_State = GameStages.BoardEditing
        m_isComputerFirst = !m_isComputerFirst
        m_playerGuessList.clear()
        m_computerGuessList.clear()
        m_gameStats.reset()
        m_computerLogic.reset()
    }

    //switches to the state GameNotStarted
    fun setRoundEnd() {
        m_State = GameStages.GameNotStarted
    }

    //checks if we are in state GameNotStarted
    fun didRoundEnd(): Boolean {
        return m_State === GameStages.GameNotStarted
    }

    /**
     * @param[in] gp - the player's guess together with its evaluation
     * @param[out] pgr - response to the player's guess: the computer's guess, if the game ended, winner, game statistics
     * Plays a step in the game, as triggered by the player's guess gp.
     */
    fun playerGuess(gp: GuessPoint): PlayerGuessReaction {
        val pgr = PlayerGuessReaction()
        if (m_State !== GameStages.Game) return pgr
        if (m_isComputerFirst) {
            updateGameStatsAndReactionComputer(pgr)
            updateGameStatsAndGuessListPlayer(gp)
        } else {
            updateGameStatsAndGuessListPlayer(gp)
            updateGameStatsAndReactionComputer(pgr)
        }
        val roundEndsResult = roundEnds()
        if (roundEndsResult.first || roundEndsResult.second) {
            if (roundEndsResult.first && roundEndsResult.second) {
                m_gameStats.addDrawResult()
                pgr.m_IsDraw = true
            } else {
                pgr.m_IsDraw = false
                m_gameStats.updateWins(roundEndsResult.second)
            }
            pgr.m_RoundEnds = true
            m_State = GameStages.GameNotStarted
            pgr.m_isPlayerWinner = roundEndsResult.first
        } else {
            pgr.m_RoundEnds = false
        }
        pgr.m_GameStats = m_gameStats
        return pgr
    }

    /**
     * @param[in] row, col - coordinates of player's guess
     * Check if a guess was already made at this position.
     */
    fun playerGuessAlreadyMade(row: Int, col: Int): Int {
        for (guess in m_playerGuessList) {
            if (guess.row() == col && guess.col() == row) return 1
        }
        return 0
    }

    /**
     * @param[in] row, col - coordinates of player's guess
     * @param[out] guessRes - the evaluation of the player's guess
     * @param[out] pgr - response to the player's guess: the computer's guess, if the game ended, winner, game statistics
     * Plays a step in the game, as triggered by the the player's guess coordinates.
     */
    fun playerGuessIncomplete(row: Int, col: Int): Pair<Type, PlayerGuessReaction> {
        val qp = Coordinate2D(col, row)
        val guessRes = m_ComputerGrid.getGuessResult(qp)
        val gp = GuessPoint(qp.x(), qp.y(), guessRes)
        val pgr = playerGuess(gp)
        return Pair(guessRes, pgr)
    }

    /**
     * Rotate the plane and return false if the current plane configuration is valid.
     */
    fun rotatePlane(idx: Int): Boolean {
        m_PlayerGrid.rotatePlane(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane left and return false if the current plane configuration is valid.
     */
    fun movePlaneLeft(idx: Int): Boolean {
        m_PlayerGrid.movePlaneLeft(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane right and return false if the current plane configuration is valid.
     */
    fun movePlaneRight(idx: Int): Boolean {
        m_PlayerGrid.movePlaneRight(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane upwards and return false if the current plane configuration is valid.
     */
    fun movePlaneUpwards(idx: Int): Boolean {
        m_PlayerGrid.movePlaneUpwards(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane downwards and return false if the current plane configuration is valid.
     */
    fun movePlaneDownwards(idx: Int): Boolean {
        m_PlayerGrid.movePlaneDownwards(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    fun doneEditing() {
        m_State = GameStages.Game
    }

    fun playerGrid(): PlaneGrid {
        return m_PlayerGrid
    }

    fun computerGrid(): PlaneGrid {
        return m_ComputerGrid
    }

    fun computerLogic(): ComputerLogic {
        return m_computerLogic
    }

    fun getRowNo(): Int {
        return m_rowNo
    }

    fun getColNo(): Int {
        return m_colNo
    }

    fun getPlaneNo(): Int {
        return m_planeNo
    }

    /*
	-2 - plane head
	-1 - plane intersection
	0 - is not on plane
	i - plane but not head
    */
    fun getPlaneSquareType(row: Int, col: Int, isComputer: Boolean): Int {
        var isOnPlane: Pair<Boolean, Int>
        if (isComputer) {
            isOnPlane = m_ComputerGrid.isPointOnPlane(row, col)
            if (!isOnPlane.first) return 0
            val annotation = m_ComputerGrid.getPlanePointAnnotation(isOnPlane.second)
            val planesIdx = m_ComputerGrid.decodeAnnotation(annotation)
            if (planesIdx.size > 1) {
                return -1
            }
            if (planesIdx.size == 1) {
                return if (planesIdx[0] < 0) -2 else planesIdx[0] + 1
            }
        } else {
            isOnPlane = m_PlayerGrid.isPointOnPlane(row, col)
            if (!isOnPlane.first) return 0
            val annotation = m_PlayerGrid.getPlanePointAnnotation(isOnPlane.second)
            val planesIdx = m_PlayerGrid.decodeAnnotation(annotation)
            if (planesIdx.size > 1) {
                return -1
            }
            if (planesIdx.size == 1) {
                return if (planesIdx[0] < 0) -2 else planesIdx[0] + 1
            }
        }
        return 0
    }

    fun getPlayerGuessesNo(): Int {
        return m_playerGuessList.size
    }
    fun getComputerGuessesNo(): Int {
        return m_computerGuessList.size
    }

    fun getPlayerGuess(idx: Int): GuessPoint {
        return if (idx < 0 || idx >= m_playerGuessList.size) GuessPoint(-1, -1, Type.Miss) else m_playerGuessList[idx].clone() as GuessPoint
    }

    fun getComputerGuess(idx: Int): GuessPoint {
        return if (idx < 0 || idx >= m_computerGuessList.size) GuessPoint(-1, -1, Type.Miss) else m_computerGuessList[idx].clone() as GuessPoint
    }

    fun getCurrentStage(): Int {
        return m_State.value
    }

    //update game statistics
    private fun updateGameStats(gp: GuessPoint, isComputer: Boolean) {
        m_gameStats.updateStats(gp, isComputer)
    }

    //tests whether all of the planes have been guessed
    private fun enoughGuesses(pg: PlaneGrid, guessList: Vector<GuessPoint>): Boolean {
        //to test draws
        //if (guessList.size() > 10)
        //   return true;
        var count = 0
        for (i in guessList.indices) {
            val gp = guessList[i]
            if (gp.type() === Type.Dead) count++
        }
        return count >= pg.planeNo
    }

    //based on the available information makes the next move for the computer
    private fun guessComputerMove(): GuessPoint {
        //use the computer strategy to get a move
        val p = m_computerLogic.makeChoice(m_RoundOptions.m_ComputerSkillLevel)

        //use the player grid to see the result of the grid
        val tp = m_PlayerGrid.getGuessResult(p.second)
        val gp = GuessPoint(p.second.x(), p.second.y(), tp)

        //add the data to the computer strategy
        m_computerLogic.addData(gp)

        //update the computer guess list
        m_computerGuessList.add(gp)
        return gp
    }

    /**
     * Sets the computer skill. When this is during a game reject the change.
     */
    fun setComputerSkill(computerSkill: Int): Boolean {
        if (m_State === GameStages.Game) return false
        m_RoundOptions.m_ComputerSkillLevel = computerSkill
        return true
    }

    /**
     * Sets the computer skill. When this is during a game reject the change.
     */
    fun setShowPlaneAfterKill(showPlane: Boolean): Boolean {
        if (m_State === GameStages.Game) return false
        m_RoundOptions.m_ShowPlaneAfterKill = showPlane
        return true
    }

    fun getComputerSkill(): Int {
        return m_RoundOptions.m_ComputerSkillLevel
    }

    fun getShowPlaneAfterKill(): Boolean {
        return m_RoundOptions.m_ShowPlaneAfterKill
    }

    private fun updateGameStatsAndGuessListPlayer(gp: GuessPoint) {
        //update the game statistics
        updateGameStats(gp, false)
        //add the player's guess to the list of guesses
        // assume that the guess is different from the other guesses
        m_playerGuessList.add(gp.clone() as GuessPoint)
        m_ComputerGrid.addGuess(gp)
        if (gp.isDead && m_RoundOptions.m_ShowPlaneAfterKill) {
            val pos = m_ComputerGrid.searchPlane(gp.row(), gp.col())
            if (pos < 0) return
            val planePointsResult = m_ComputerGrid.getPlanePoints(pos)
            val planePoints = planePointsResult.second
            for (i in planePoints.indices) {
                val gp1 = GuessPoint(planePoints[i].x(), planePoints[i].y(), Type.Hit)
                if (!m_playerGuessList.contains(gp1)) {
                    m_playerGuessList.add(gp1.clone() as GuessPoint)
                    m_ComputerGrid.addGuess(gp1)
                }
            }
        }
    }

    private fun updateGameStatsAndReactionComputer(pgr: PlayerGuessReaction) {
        val gpc = guessComputerMove()
        m_PlayerGrid.addGuess(gpc)
        updateGameStats(gpc, true)
        pgr.m_ComputerMoveGenerated = true
        pgr.m_ComputerGuess = gpc
    }

    //resets the round
    private fun reset() {
        m_PlayerGrid.resetGrid()
        m_ComputerGrid.resetGrid()
        m_playerGuessList.clear()
        m_computerGuessList.clear()
        m_gameStats.reset()
        m_computerLogic.reset()
    }

    //check to see if there is a winner
    private fun roundEnds(): Pair<Boolean, Boolean> {
        val computerFinished = enoughGuesses(m_PlayerGrid, m_computerGuessList)
        val playerFinished = enoughGuesses(m_ComputerGrid, m_playerGuessList)
        return Pair.create(playerFinished, computerFinished)
    }


}