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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View window = null;
        window = inflater.inflate(R.layout.start_new_game_vertical_phone, container, false);
        createGUIMembers(window);
        return window;
    }

    public void createGUIMembers(View window) {
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
