package com.planes.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
            LayoutInflater layoutinflater = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View window = layoutinflater.inflate(R.layout.board_editing_controls, null);
            setRowCount(1);
            setColumnCount(1);
            GridLayout.LayoutParams paramsWindow = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(0, 1));
            paramsWindow.setGravity(Gravity.CENTER);
            addView(window, paramsWindow);
            m_RotateButton = (Button)findViewById(R.id.rotate_button);
            m_LeftButton = (Button)findViewById(R.id.left_button);
            m_RightButton = (Button)findViewById(R.id.right_button);
            m_UpButton = (Button)findViewById(R.id.up_button);
            m_DownButton = (Button)findViewById(R.id.down_button);
            m_DoneButton = (Button)findViewById(R.id.done_button);
        }
    }

    public void setGameSettings(PlaneRoundJavaFx planeRound) {
        m_PlaneRound = planeRound;
        init(m_Context, GameStages.BoardEditing);
    }

    //TODO: to delete
    public void setBoardEditingControls(Context context) {
            setRowCount(3);
            setColumnCount(4);
            //TODO: add the image buttons
            GridLayout.LayoutParams paramsRotate = new GridLayout.LayoutParams(GridLayout.spec(1, 1), GridLayout.spec(1, 1));
            paramsRotate.setGravity(Gravity.CENTER_VERTICAL);
            m_RotateButton = new Button(context);
            m_RotateButton.setText("Rotate");
            addView(m_RotateButton, paramsRotate);
            //invalidate();

            m_LeftButton = new Button(context);
            m_LeftButton.setText("Left");
            GridLayout.LayoutParams paramsLeft = new GridLayout.LayoutParams(GridLayout.spec(1, 1), GridLayout.spec(0, 1));
            paramsLeft.setGravity(Gravity.CENTER_VERTICAL);
            addView(m_LeftButton, paramsLeft);

            m_RightButton = new Button(context);
            m_RightButton.setText("Right");
            GridLayout.LayoutParams paramsRight = new GridLayout.LayoutParams(GridLayout.spec(1, 1), GridLayout.spec(2, 1));
            paramsRight.setGravity(Gravity.CENTER_VERTICAL);
            addView(m_RightButton, paramsRight);

            m_UpButton = new Button(context);
            m_UpButton.setText("Up");
            GridLayout.LayoutParams paramsUp = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(1, 1));
            addView(m_UpButton, paramsUp);

            m_DownButton = new Button(context);
            m_DownButton.setText("Down");
            GridLayout.LayoutParams paramsDown = new GridLayout.LayoutParams(GridLayout.spec(2, 1), GridLayout.spec(1, 1));
            //paramsDown.setGravity(Gravity.CENTER);
            addView(m_DownButton, paramsDown);

            m_DoneButton = new Button(context);
            m_DoneButton.setText("Done");
            GridLayout.LayoutParams paramsDone = new GridLayout.LayoutParams(GridLayout.spec(1, 1), GridLayout.spec(3, 1));
            paramsDone.setGravity(Gravity.CENTER_VERTICAL);
            addView(m_DoneButton, paramsDone);
    }

    private Context m_Context;
    private GameStages m_CurStage = GameStages.BoardEditing;
    private PlaneRoundJavaFx m_PlaneRound;
    private Button m_RotateButton;
    private Button m_LeftButton;
    private Button m_RightButton;
    private Button m_UpButton;
    private Button m_DownButton;
    private Button m_DoneButton;
}
