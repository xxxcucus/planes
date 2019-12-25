package com.planes.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;


//horizontal layout works with 2 GameBoard objects and 2 GameControl objects
//the game control object should change depending on the game stage
//the size of the game controls depends on the size of the visible game board
//dimension of the screen and toolbars should be saved inside the layout
public class PlanesTabletHorizontalLayout extends ViewGroup {

    public PlanesTabletHorizontalLayout(Context context) {
        super(context);
        m_Context = context;
    }

    public PlanesTabletHorizontalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Context = context;
    }

    public PlanesTabletHorizontalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_Context = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        if (count != 4)
            return;
    }

    private Context m_Context;
    private GameStages m_GameStage;
    private boolean m_CorrectChildren;
    //there should be maximum 2 gameboards, all the other should have gc_row, gc_col, gc_game_stage
    //the ones that do not have the required gc_ parameters are discarded
}
