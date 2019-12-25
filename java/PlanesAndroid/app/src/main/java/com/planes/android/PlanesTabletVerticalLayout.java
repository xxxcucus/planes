package com.planes.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;


//vertical layout works with 2 GameBoard objects and 2 GameControl objects
//the game control object should change depending on the game stage
//the size of the game controls depends on the size of the visible game board
//dimension of the screen and toolbars should be saved inside the layout
public class PlanesTabletVerticalLayout extends ViewGroup {

    public PlanesTabletVerticalLayout(Context context) {
        super(context);
        m_Context = context;
    }

    public PlanesTabletVerticalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Context = context;
    }

    public PlanesTabletVerticalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_Context = context;
    }

    private Context m_Context;
    private GameStages m_GameStage;
    private boolean m_CorrectChildren;
}
