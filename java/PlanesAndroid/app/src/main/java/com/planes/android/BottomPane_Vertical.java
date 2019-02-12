package com.planes.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.TabHost;

//bottom pane in a vertical layout
public class BottomPane_Vertical extends GridLayout {

    public enum GameStages {
        GameNotStarted, BoardEditing, Game
    }

    public BottomPane_Vertical(Context context) {
        super(context);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    public BottomPane_Vertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    public BottomPane_Vertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    private void init(Context context) {
        //TODO: build a grid layout with left, right, up, down, rotate, and done

    }

    Context m_Context;
}
