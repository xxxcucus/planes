package com.planes.android;


import android.support.v4.app.FragmentActivity;

/**
 * Class that manages the control objects: the buttons to position the planes,
 * the statistics, the buttons to toggle between game board views, start new game
 * button, button to mark the end of the board editing stage.
 */
public class ControlWidgets {
    public enum GameStages {
        GameNotStarted, BoardEditing, Game
    }

    ControlWidgets(FragmentActivity activity) {
        m_Activity = activity;
    }


    private void showBoardEditing() {
        m_CurStage = GameStages.BoardEditing;

        if (m_Tablet) {
            if (m_Vertical) {
                m_BoardEditingFragment = new BoardEditingTabletVerticalFragment();
                m_Activity.getSupportFragmentManager().beginTransaction().
                    add(R.id.bottom_pane_player, m_BoardEditingFragment).commit();
            } else {
                m_BoardEditingFragment = new BoardEditingPhoneFragment();
                m_Activity.getSupportFragmentManager().beginTransaction().
                    add(R.id.bottom_pane_player, m_BoardEditingFragment).commit();
            }
        } else {
            m_BoardEditingFragment = new BoardEditingPhoneFragment();
            m_Activity.getSupportFragmentManager().beginTransaction().
                    add(R.id.bottom_pane_player, m_BoardEditingFragment).commit();
        }
    }


    /**
     * Remove all fragments
      */
    public void resetGUI() {
        if (m_Tablet) {
            m_Activity.getSupportFragmentManager().beginTransaction().
                remove(m_Activity.getSupportFragmentManager().findFragmentById(R.id.bottom_pane_player)).commit();
            m_Activity.getSupportFragmentManager().beginTransaction().
                remove(m_Activity.getSupportFragmentManager().findFragmentById(R.id.bottom_pane_computer)).commit();
        } else {
            m_Activity.getSupportFragmentManager().beginTransaction().
                remove(m_Activity.getSupportFragmentManager().findFragmentById(R.id.bottom_pane)).commit();
        }
    }

    public void setBoardWidgets(BoardWidgets boards) {
        m_BoardWidgets = boards;
    }

    private GameStages m_CurStage = GameStages.BoardEditing;
    private BoardWidgets m_BoardWidgets;
    FragmentActivity m_Activity;
    private boolean m_Tablet = false;
    private boolean m_Vertical = false;

    BoardEditingPhoneFragment m_BoardEditingFragment = null;
}
