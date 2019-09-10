package com.planes.android;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.planes.javafx.PlaneRoundJavaFx;

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
        FragmentManager fragmentManager = m_Activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (m_Tablet) {
            if (m_Vertical) {
                m_BoardEditingFragment = new BoardEditingTabletVerticalFragment();
            } else {
                m_BoardEditingFragment = new BoardEditingPhoneFragment();
            }
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane_player));
            fragmentTransaction.add(R.id.bottom_pane_player, m_BoardEditingFragment);
            fragmentTransaction.commit();
        } else {
            m_BoardEditingFragment = new BoardEditingPhoneFragment();
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane));
            fragmentTransaction.add(R.id.bottom_pane, m_BoardEditingFragment);
            fragmentTransaction.commit();
        }

        //TODO: de-reference other fragments
    }

    private void showGame() {
        m_CurStage = GameStages.BoardEditing;
        FragmentManager fragmentManager = m_Activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (m_Tablet) {
            m_GamePlayerFragment = new GameStatsFragment();
            m_GameComputerFragment = new GameStatsFragment();
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane_player));
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane_computer));
            fragmentTransaction.add(R.id.bottom_pane_player, m_GamePlayerFragment);
            fragmentTransaction.add(R.id.bottom_pane_computer, m_GameComputerFragment);
            fragmentTransaction.commit();

        } else {
            m_GamePhoneFragment = new GameStatsWithToggleBoardButtonsFragment();
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane));
            fragmentTransaction.add(R.id.bottom_pane, m_GamePhoneFragment);
            fragmentTransaction.commit();
        }

        //TODO: de-reference other fragments
    }

    private void showGameNotStarted() {
        m_CurStage = GameStages.GameNotStarted;
        FragmentManager fragmentManager = m_Activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (m_Tablet) {
            m_StartNewGameFragment = new StartNewGameFragment();
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane_player));
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane_computer));
            fragmentTransaction.add(R.id.bottom_pane_player, m_StartNewGameFragment);
            fragmentTransaction.commit();

        } else {
            m_StartNewGameFragment = new StartNewGameToggleButtonsFragment();
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.bottom_pane));
            fragmentTransaction.add(R.id.bottom_pane, m_StartNewGameFragment);
            fragmentTransaction.commit();
        }

        //TODO: de-reference other fragments
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

    public void setGameSettings(PlaneRoundJavaFx planeRound, boolean isTablet, boolean isVertical) {
        m_PlaneRound = planeRound;
        m_Tablet = isTablet;
        m_Vertical = isVertical;
        resetGUI();
        showBoardEditing();
    }

    public void setBoardWidgets(BoardWidgets boards) {
        m_BoardWidgets = boards;
    }

    public void setDoneEnabled(boolean enabled) {
        m_BoardEditingFragment.setDoneEnabled(enabled);
    }

    public GameStages getGameStage() {
        return m_CurStage;
    }

    public void roundEnds(int playerWins, int computerWins, boolean isComputerWinner) {
        showGameNotStarted();
        int computer_wins = m_PlaneRound.playerGuess_StatNoComputerWins();
        int player_wins = m_PlaneRound.playerGuess_StatNoPlayerWins();

        String winnerText = null;
        if (isComputerWinner)
            winnerText = m_Activity.getResources().getText(R.string.computer_winner).toString();
        else
            winnerText = m_Activity.getResources().getText(R.string.player_winner).toString();

        m_StartNewGameFragment.updateStats(computer_wins, player_wins, winnerText);
    }

    public void updateStats(boolean isComputer) {
        if (isComputer) {
            int misses = m_PlaneRound.playerGuess_StatNoPlayerMisses();
            int hits = m_PlaneRound.playerGuess_StatNoPlayerHits();
            int dead = m_PlaneRound.playerGuess_StatNoPlayerDead();
            int moves = m_PlaneRound.playerGuess_StatNoPlayerMoves();

            String hitsText = m_Activity.getResources().getString(R.string.player_hits);
            String deadText = m_Activity.getResources().getString(R.string.player_dead);
            String missesText = m_Activity.getResources().getString(R.string.player_misses);
            String movesText = m_Activity.getResources().getString(R.string.player_moves);

            if (m_Tablet) {
                m_GameComputerFragment.updateStats(misses, hits, dead, moves, missesText, hitsText, deadText, movesText);
            } else {
                m_GamePhoneFragment.updateStats(misses, hits, dead, moves, missesText, hitsText, deadText, movesText);
            }

        } else {
            int misses = m_PlaneRound.playerGuess_StatNoComputerMisses();
            int hits = m_PlaneRound.playerGuess_StatNoComputerHits();
            int dead = m_PlaneRound.playerGuess_StatNoComputerDead();
            int moves = m_PlaneRound.playerGuess_StatNoComputerMoves();

            String hitsText = m_Activity.getResources().getString(R.string.computer_hits);
            String deadText = m_Activity.getResources().getString(R.string.computer_dead);
            String missesText = m_Activity.getResources().getString(R.string.computer_misses);
            String movesText = m_Activity.getResources().getString(R.string.computer_moves);

            if (m_Tablet) {
                m_GamePlayerFragment.updateStats(misses, hits, dead, moves, missesText, hitsText, deadText, movesText);
            } else {
                m_GamePhoneFragment.updateStats(misses, hits, dead, moves, missesText, hitsText, deadText, movesText);
            }
        }
    }

    private GameStages m_CurStage = GameStages.BoardEditing;
    private BoardWidgets m_BoardWidgets;
    FragmentActivity m_Activity;
    private boolean m_Tablet = false;
    private boolean m_Vertical = false;

    private BoardEditingPhoneFragment m_BoardEditingFragment = null;
    private GameStatsFragment m_GamePhoneFragment = null;
    private GameStatsFragment m_GamePlayerFragment = null;
    private GameStatsFragment m_GameComputerFragment = null;
    private StartNewGameFragment m_StartNewGameFragment = null;

    private PlaneRoundJavaFx m_PlaneRound;
}
