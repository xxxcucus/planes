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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View window = null;
        window = inflater.inflate(R.layout.start_new_game_vertical_tablet, container, false);
        createGUIMembers(window);
        return window;
    }

    public void createGUIMembers(View window) {
        m_ComputerWins = (TextView)window.findViewById(R.id.computer_wins_count);
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
        m_ComputerWins.setText(Integer.toString(computer_wins));
        m_PlayerWins.setText(Integer.toString(player_wins));
        m_WinnerTextView.setText(winnerText);
    }

    protected TextView m_WinnerTextView;
    protected Button m_StartNewRound;
    protected TextView m_ComputerWins;
    protected TextView m_PlayerWins;

    protected OnControlButtonListener m_ButtonClickListener = null;
}
