package com.planes.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StartNewGameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView StartNewGameFragment");
        View window = null;
        window = inflater.inflate(R.layout.start_new_game_vertical_tablet, container, false);
        createGUIMembers(window);
        if (m_ComputerWins != null)
            m_ComputerWins.setText(Integer.toString(m_ComputerWinsCount));
        if (m_PlayerWins != null)
            m_PlayerWins.setText(Integer.toString(m_PlayerWinsCount));
        if (m_WinnerTextView != null)
            m_WinnerTextView.setText(m_WinnerText);
        return window;
    }

    public void createGUIMembers(View window) {
        m_ComputerWins = (TextView)window.findViewById(R.id.computer_wins_count);
        System.out.println("CreateGUIMembers "+ m_ComputerWins);
        m_PlayerWins = (TextView)window.findViewById(R.id.player_wins_count);
        m_StartNewRound = (Button)window.findViewById(R.id.start_new_game);
        m_WinnerTextView = (TextView)window.findViewById(R.id.winner_textview);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        m_StartNewRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            m_ButtonClickListener.onControlButtonClicked("start_new_game");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            m_ButtonClickListener = (OnControlButtonListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnControlButtonListener");
        }
    }

    public void updateStats(int computer_wins, int player_wins, String winnerText) {
        System.out.println("Update stats " + computer_wins + " " + player_wins + " " + winnerText);
        m_ComputerWinsCount = computer_wins;
        m_PlayerWinsCount = player_wins;
        m_WinnerText = winnerText;
        if (m_ComputerWins != null)
            m_ComputerWins.setText(Integer.toString(m_ComputerWinsCount));
        if (m_PlayerWins != null)
            m_PlayerWins.setText(Integer.toString(m_PlayerWinsCount));
        if (m_WinnerTextView != null)
            m_WinnerTextView.setText(m_WinnerText);
    }

    public void setButtonsEnabled(boolean isComputer) {
    }

    protected TextView m_WinnerTextView;
    protected Button m_StartNewRound;
    protected TextView m_ComputerWins;
    protected TextView m_PlayerWins;

    protected int m_ComputerWinsCount = 0;
    protected int m_PlayerWinsCount = 0;
    protected String m_WinnerText = "";

    protected OnControlButtonListener m_ButtonClickListener = null;
}
