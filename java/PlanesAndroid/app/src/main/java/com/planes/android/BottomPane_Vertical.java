package com.planes.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;

import com.planes.javafx.PlaneRoundJavaFx;


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

    private void init(Context context, GameStages gameStage) {
        //TODO: build a grid layout with left, right, up, down, rotate, and done
        m_CurStage = gameStage;
        //removeAllViews();

        if (m_CurStage == GameStages.BoardEditing) {
            System.out.println("Set rotate button");
            setRowCount(1);
            setColumnCount(1);
            //TODO: add the image buttons
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(0, 1));
            params.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

            m_RotateButton = new Button(context);
            m_RotateButton.setText("Rotate");
            addView(m_RotateButton, params);
            //invalidate();
        }
    }

    public void setGameSettings(PlaneRoundJavaFx planeRound) {
        m_PlaneRound = planeRound;
        init(m_Context, GameStages.BoardEditing);
    }

    private Context m_Context;
    private GameStages m_CurStage = GameStages.BoardEditing;
    private PlaneRoundJavaFx m_PlaneRound;
    private Button m_RotateButton;
}
