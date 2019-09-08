package com.planes.android;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameStatsWithToggleBoardButtonsFragment extends GameStatsFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View window = null;
        window = inflater.inflate(R.layout.game_stats_phone_vertical, container, false);
        createGUIMembers(window);
        return window;
    }

    public void createGUIMembers(View window) {
        super.createGUIMembers(window);
        m_ViewPlayerBoardButton = (Button)window.findViewById(R.id.view_player_board1);
        m_ViewComputerBoardButton = (Button)window.findViewById(R.id.view_computer_board1);
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


    protected Button m_ViewPlayerBoardButton;
    protected Button m_ViewComputerBoardButton;
    protected OnControlButtonListener m_ButtonClickListener = null;
}
