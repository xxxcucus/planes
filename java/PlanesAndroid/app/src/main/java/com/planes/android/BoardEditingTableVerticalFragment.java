package com.planes.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BoardEditingTableVerticalFragment extends BoardEditingPhoneFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View window = null;
        window = inflater.inflate(R.layout.board_editing_controls_horizontal, container, false);
        createButtons(window);
        return window;
    }
}
