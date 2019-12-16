package com.planes.android;

public class GameBoardsAdaptor {

    public GameBoardsAdaptor(GameBoard gameBoard) {
        m_Tablet = false;
        m_GameBoard = gameBoard;
    }

    public GameBoardsAdaptor(GameBoard playerBoard, GameBoard computerBoard) {
        m_Tablet = true;
        m_PlayerBoard = playerBoard;
        m_ComputerBoard = computerBoard;
    }

    public void setGameControls(GameControls controls) {
        if (m_Tablet) {
            m_PlayerBoard.setGameControls(controls);
            m_ComputerBoard.setGameControls(controls);
        } else {
            m_GameBoard.setGameControls(controls);
        }
    }

    public void setNewRoundStage() {
        if (m_Tablet) {
            m_PlayerBoard.setNewRoundStage(false);
            m_ComputerBoard.setNewRoundStage(false);
        } else {
            m_GameBoard.setNewRoundStage(true);
        }
    }

    public void setBoardEditingStage() {
        if (m_Tablet) {
            m_PlayerBoard.setBoardEditingStage(false);
            m_ComputerBoard.setBoardEditingStage(false);
        } else {
            m_GameBoard.setBoardEditingStage(true);
        }
    }

    public void setGameStage() {
        if (m_Tablet) {
            m_PlayerBoard.setGameStage(false);
            m_ComputerBoard.setGameStage(false);
        } else {
            m_GameBoard.setGameStage(true);
        }
    }

    public void movePlaneLeft() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneLeft();
        } else {
            m_GameBoard.movePlaneLeft();
        }
    }

    public void movePlaneRight() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneRight();
        } else {
            m_GameBoard.movePlaneRight();
        }
    }

    public void movePlaneUp() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneUp();
        } else {
            m_GameBoard.movePlaneUp();
        }
    }

    public void movePlaneDown() {
        if (m_Tablet) {
            m_PlayerBoard.movePlaneDown();
        } else {
            m_GameBoard.movePlaneDown();
        }
    }

    public void rotatePlane() {
        if (m_Tablet) {
            m_PlayerBoard.rotatePlane();
        } else {
            m_GameBoard.rotatePlane();
        }
    }

    public void setPlayerBoard() {
        if (!m_Tablet)
            m_GameBoard.setPlayerBoard();
    }

    public void setComputerBoard() {
        if (!m_Tablet)
            m_GameBoard.setComputerBoard();
    }

    public GameBoard.GameStages getGameStage() {
        if (!m_Tablet) {
            return m_GameBoard.getGameStage();
        } else {
            return m_PlayerBoard.getGameStage();
        }
    }

    //used when playing on a phone
    private GameBoard m_GameBoard = null;
    //used when playing on a tablet
    private GameBoard m_PlayerBoard = null;
    private GameBoard m_ComputerBoard = null;

    private boolean m_Tablet = false;
}
