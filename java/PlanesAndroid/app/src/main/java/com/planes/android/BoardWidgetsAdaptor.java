package com.planes.android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.planes.javafx.PlaneRoundJavaFx;

/**
 * Class that manages the game board graphical objects.
 * For phones only one game board is displayed at a time.
 * For tables both game board (player and computer) are displayed at the same time.
 */

public class BoardWidgetsAdaptor {


    public BoardWidgetsAdaptor() {
    }

    /**
     * Depending on which layout is actually used initialize the necessary
     * TopPane_Vertical objects. Saves if the device is a tablet or phone
     * and checks for errors.
     */

    public boolean init(Activity activity) {
        m_BoardWidgetsPhone = (GameBoard)activity.findViewById(R.id.top_pane);
        m_BoardWidgetsComputerTablet = (GameBoard)activity.findViewById(R.id.top_pane_computer);
        m_BoardWidgetsPlayerTablet = (GameBoard)activity.findViewById(R.id.top_pane_player);

        if (m_BoardWidgetsPhone != null && m_BoardWidgetsComputerTablet == null && m_BoardWidgetsPlayerTablet == null) {
            m_Tablet = false;
            m_IsCorrect = true;
        } else if (m_BoardWidgetsPhone == null && m_BoardWidgetsComputerTablet != null && m_BoardWidgetsPlayerTablet != null) {
            m_Tablet = true;
            m_IsCorrect = true;
        } else {
            m_IsCorrect = false;
        }

        if (m_Tablet) {
            Log.d("Planes", "Tablet");
        } else {
            Log.d("Planes", "Phone");
        }
        if (!m_IsCorrect) {
            Log.e("Planes", "Error: False layout configuration");
            activity.finishAffinity();
        }

        return m_Tablet;
    }


    public void setGameSettings(PlaneRoundJavaFx planeRound) {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.setGameSettings(planeRound, m_Tablet);
            m_BoardWidgetsComputerTablet.setGameSettings(planeRound, m_Tablet);
        } else {
            m_BoardWidgetsPhone.setGameSettings(planeRound, m_Tablet);
        }

        if (m_Tablet) {
            m_BoardWidgetsComputerTablet.setComputerBoard();
            m_BoardWidgetsPlayerTablet.setPlayerBoard();
            m_BoardWidgetsComputerTablet.setPairBoard(m_BoardWidgetsPlayerTablet);
            m_BoardWidgetsPlayerTablet.setPairBoard(m_BoardWidgetsComputerTablet);
        }
    }

    public void setBoardControls(ControlWidgetsAdaptor controls) {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.setBoardControls(controls);
            m_BoardWidgetsComputerTablet.setBoardControls(controls);
        } else {
            m_BoardWidgetsPhone.setBoardControls(controls);
        }
    }

    public void setNewRoundStage() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.setNewRoundStage(false);
            m_BoardWidgetsComputerTablet.setNewRoundStage(false);
        } else {
            m_BoardWidgetsPhone.setNewRoundStage(true);
        }
    }

    public void setBoardEditingStage() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.setBoardEditingStage(false);
            m_BoardWidgetsComputerTablet.setBoardEditingStage(false);
        } else {
            m_BoardWidgetsPhone.setBoardEditingStage(true);
        }
    }

    public void setGameStage() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.setGameStage(false);
            m_BoardWidgetsComputerTablet.setGameStage(false);
        } else {
            m_BoardWidgetsPhone.setGameStage(true);
        }
    }

    public void movePlaneLeft() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.movePlaneLeft();
        } else {
            m_BoardWidgetsPhone.movePlaneLeft();
        }
    }

    public void movePlaneRight() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.movePlaneRight();
        } else {
            m_BoardWidgetsPhone.movePlaneRight();
        }
    }

    public void movePlaneUp() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.movePlaneUp();
        } else {
            m_BoardWidgetsPhone.movePlaneUp();
        }
    }

    public void movePlaneDown() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.movePlaneDown();
        } else {
            m_BoardWidgetsPhone.movePlaneDown();
        }
    }

    public void rotatePlane() {
        if (m_Tablet) {
            m_BoardWidgetsPlayerTablet.rotatePlane();
        } else {
            m_BoardWidgetsPhone.rotatePlane();
        }
    }

    public void setPlayerBoard() {
        if (!m_Tablet)
            m_BoardWidgetsPhone.setPlayerBoard();
    }

    public void setComputerBoard() {
        if (!m_Tablet)
            m_BoardWidgetsPhone.setComputerBoard();
    }


    private GameBoard m_BoardWidgetsPhone = null;
    private GameBoard m_BoardWidgetsPlayerTablet = null;
    private GameBoard m_BoardWidgetsComputerTablet = null;
    boolean m_Tablet = false;
    boolean m_IsCorrect = false;
    private Context m_Context;
}
