package com.planes.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BoardEditingPhoneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View window = null;
        window = inflater.inflate(R.layout.board_editing_controls_vertical, container, false);
        createButtons(window);
        return window;
    }

    public void createButtons(View window) {
        m_RotateButton = (Button)window.findViewById(R.id.rotate_button);
        m_LeftButton = (Button)window.findViewById(R.id.left_button);
        m_RightButton = (Button)window.findViewById(R.id.right_button);
        m_UpButton = (Button)window.findViewById(R.id.up_button);
        m_DownButton = (Button)window.findViewById(R.id.down_button);
        m_DoneButton = (Button)window.findViewById(R.id.done_button);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        m_LeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onBoardEditingButtonClicked("left");
            }
        });

        m_RightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onBoardEditingButtonClicked("right");
            }
        });

        m_UpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onBoardEditingButtonClicked("up");
            }
        });

        m_DownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onBoardEditingButtonClicked("down");
            }
        });

        m_RotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onBoardEditingButtonClicked("rotate");
            }
        });

        m_DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_ButtonClickListener.onBoardEditingButtonClicked("done");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            m_ButtonClickListener = (OnBoardEditingButtonListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnBoardEditingButtonListener");
        }
    }

    protected Button m_RotateButton;
    protected Button m_LeftButton;
    protected Button m_RightButton;
    protected Button m_UpButton;
    protected Button m_DownButton;
    protected Button m_DoneButton;

    protected OnBoardEditingButtonListener m_ButtonClickListener = null;
}
