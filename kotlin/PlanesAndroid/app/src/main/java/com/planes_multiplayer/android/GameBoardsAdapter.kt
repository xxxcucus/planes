package com.planes.android

import com.planes_multiplayer.single_player_engine.GameStages

class GameBoardsAdapter {
    //used when playing on a phone
    private lateinit var m_GameBoard: GameBoard

    //used when playing on a tablet
    private lateinit var m_PlayerBoard: GameBoard
    private lateinit var m_ComputerBoard: GameBoard
    private lateinit var m_GameControls: GameControlsAdapter
    private var m_Tablet = false

    constructor(gameBoard: GameBoard) {
        m_Tablet = false
        m_GameBoard = gameBoard
    }

    constructor(playerBoard: GameBoard, computerBoard: GameBoard) {
        m_Tablet = true
        m_PlayerBoard = playerBoard
        m_ComputerBoard = computerBoard
        m_ComputerBoard.setSiblingBoard(m_PlayerBoard)
    }

    fun setGameControls(controls: GameControlsAdapter) {
        if (m_Tablet) {
            m_PlayerBoard.setGameControls(controls)
            m_ComputerBoard.setGameControls(controls)
        } else {
            m_GameBoard.setGameControls(controls)
        }
        m_GameControls = controls
    }

    fun setNewRoundStage() {
        if (m_Tablet) {
            m_PlayerBoard.setNewRoundStage(false)
            m_ComputerBoard.setNewRoundStage(false)
        } else {
            m_GameBoard.setNewRoundStage(true)
        }
    }

    fun setBoardEditingStage() {
        if (m_Tablet) {
            m_PlayerBoard.setBoardEditingStage(false)
            m_ComputerBoard.setBoardEditingStage(false)
        } else {
            m_GameBoard.setBoardEditingStage(true)
        }
    }

    fun setGameStage() {
        if (m_Tablet) {
            m_PlayerBoard.setGameStage(false)
            m_ComputerBoard.setGameStage(false)
        } else {
            m_GameBoard.setGameStage(true)
        }
    }

    fun movePlaneLeft() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneLeft()
        } else {
            m_GameBoard.movePlaneLeft()
        }
    }

    fun movePlaneRight() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneRight()
        } else {
            m_GameBoard.movePlaneRight()
        }
    }

    fun movePlaneUp() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneUp()
        } else {
            m_GameBoard.movePlaneUp()
        }
    }

    fun movePlaneDown() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneDown()
        } else {
            m_GameBoard.movePlaneDown()
        }
    }

    fun rotatePlane() {
        if (m_Tablet) {
            m_PlayerBoard.rotatePlane()
        } else {
            m_GameBoard.rotatePlane()
        }
    }

    fun setPlayerBoard() {
        if (!m_Tablet) m_GameBoard.setPlayerBoard()
    }

    fun setComputerBoard() {
        if (!m_Tablet) m_GameBoard.setComputerBoard()
    }

    val gameStage: GameStages
        get() = if (!m_Tablet) {
            m_GameBoard.getGameStage()
        } else {
            m_PlayerBoard.getGameStage()
        }

}