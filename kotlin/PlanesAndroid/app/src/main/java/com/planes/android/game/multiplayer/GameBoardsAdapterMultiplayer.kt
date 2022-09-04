package com.planes.android.game.multiplayer

import com.planes.single_player_engine.GameStages

//TODO: derive from basis class the same as GameBoardsAdapterSinglePlayer
class GameBoardsAdapterMultiplayer {
    //used when playing on a phone
    private lateinit var m_GameBoard: GameBoardMultiplayer

    //used when playing on a tablet
    private lateinit var m_PlayerBoard: GameBoardMultiplayer
    private lateinit var m_ComputerBoard: GameBoardMultiplayer
    private lateinit var m_GameControls: GameControlsAdapterMultiplayer
    private var m_Tablet = false

    constructor(gameBoard: GameBoardMultiplayer) {
        m_Tablet = false
        m_GameBoard = gameBoard
    }

    constructor(playerBoard: GameBoardMultiplayer, computerBoard: GameBoardMultiplayer) {
        m_Tablet = true
        m_PlayerBoard = playerBoard
        m_ComputerBoard = computerBoard
        m_ComputerBoard.setSiblingBoard(m_PlayerBoard)
    }

    private fun showTwoBoards(): Boolean {
        return false
    }

    fun setGameControls(controls: GameControlsAdapterMultiplayer) {
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