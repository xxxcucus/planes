package com.planes.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StartNewGameToggleButtonsFragment extends StartNewGameFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("onCreateView StartNewGameToggleButtonsFragment");
        View window = null;
        window = inflater.inflate(R.layout.start_new_game_vertical_phone, container, false);
        createGUIMembers(window);
        if (m_ComputerWins != null)
            m_ComputerWins.setText(Integer.toString(m_ComputerWinsCount));
        if (m_PlayerWins != null)
            m_PlayerWins.setText(Integer.toString(m_PlayerWinsCount));
        if (m_WinnerTextView != null)
            m_WinnerTextView.setText(m_WinnerText);
        return window;
    }

    @Override
    public void createGUIMembers(View window) {
        System.out.println("createGUIMembers StartNewGameToggleButtonsFragment");
        super.createGUIMembers(window);
        m_ViewPlayerBoardButton = (Button)window.findViewById(R.id.view_player_board2);
        m_ViewComputerBoardButton = (Button)window.findViewById(R.id.view_computer_board2);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        m_ViewPlayerBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onControlButtonClicked("view_player_board");
            }
        });
        m_ViewComputerBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onControlButtonClicked("view_computer_board");
            }
        });
    }


    private Button m_ViewPlayerBoardButton;
    private Button m_ViewComputerBoardButton;
}
