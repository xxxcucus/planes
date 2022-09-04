package com.planes.android.game.singleplayer

import com.planes.single_player_engine.GameStages

class GameBoardsAdapterSinglePlayer {
    //used when playing on a phone
    private lateinit var m_GameBoard: GameBoardSinglePlayer

    //used when playing on a tablet
    private lateinit var m_PlayerBoard: GameBoardSinglePlayer
    private lateinit var m_ComputerBoard: GameBoardSinglePlayer
    private lateinit var m_GameControls: GameControlsAdapterSinglePlayer
    private var m_Tablet = false

    constructor(gameBoard: GameBoardSinglePlayer) {
        m_Tablet = false
        m_GameBoard = gameBoard
    }

    constructor(playerBoard: GameBoardSinglePlayer, computerBoard: GameBoardSinglePlayer) {
        m_Tablet = true
        m_PlayerBoard = playerBoard
        m_ComputerBoard = computerBoard
        m_ComputerBoard.setSiblingBoard(m_PlayerBoard)
    }

    private fun showTwoBoards(): Boolean {
        return m_Tablet
    }

    fun setGameControls(controls: GameControlsAdapterSinglePlayer) {
        if (showTwoBoards()) {
            m_PlayerBoard.setGameControls(controls)
            m_ComputerBoard.setGameControls(controls)
        } else {
            m_GameBoard.setGameControls(controls)
        }
        m_GameControls = controls
    }

    fun setNewRoundStage() {
        if (showTwoBoards()) {
            m_PlayerBoard.setNewRoundStage(false)
            m_ComputerBoard.setNewRoundStage(false)
        } else {
            m_GameBoard.setNewRoundStage(true)
        }
    }

    fun setBoardEditingStage() {
        if (showTwoBoards()) {
            m_PlayerBoard.setBoardEditingStage(false)
            m_ComputerBoard.setBoardEditingStage(false)
        } else {
            m_GameBoard.setBoardEditingStage(true)
        }
    }

    fun setGameStage() {
        if (showTwoBoards()) {
            m_PlayerBoard.setGameStage(false)
            m_ComputerBoard.setGameStage(false)
        } else {
            m_GameBoard.setGameStage(true)
        }
    }

    fun movePlaneLeft() {
        if (showTwoBoards()) {
            m_PlayerBoard.movePlaneLeft()
        } else {
            m_GameBoard.movePlaneLeft()
        }
    }

    fun movePlaneRight() {
        if (showTwoBoards()) {
            m_PlayerBoard.movePlaneRight()
        } else {
            m_GameBoard.movePlaneRight()
        }
    }

    fun movePlaneUp() {
        if (showTwoBoards()) {
            m_PlayerBoard.movePlaneUp()
        } else {
            m_GameBoard.movePlaneUp()
        }
    }

    fun movePlaneDown() {
        if (showTwoBoards()) {
            m_PlayerBoard.movePlaneDown()
        } else {
            m_GameBoard.movePlaneDown()
        }
    }

    fun rotatePlane() {
        if (showTwoBoards()) {
            m_PlayerBoard.rotatePlane()
        } else {
            m_GameBoard.rotatePlane()
        }
    }

    fun setPlayerBoard() {
        if (!showTwoBoards()) m_GameBoard.setPlayerBoard()
    }

    fun setComputerBoard() {
        if (!showTwoBoards()) m_GameBoard.setComputerBoard()
    }

    val gameStage: GameStages
        get() = if (!showTwoBoards()) {
            m_GameBoard.getGameStage()
        } else {
            m_PlayerBoard.getGameStage()
        }

}