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

    private void init(GameStages gameStage) {
        //TODO: build a grid layout with left, right, up, down, rotate, and done
        m_CurStage = gameStage;
        removeAllViews();

        if (m_CurStage == GameStages.BoardEditing)
            showBoardEditing();

        if (m_CurStage == GameStages.Game)
            showGame();
    }

    public void setGameSettings(PlaneRoundJavaFx planeRound) {
        m_PlaneRound = planeRound;
        init(GameStages.BoardEditing);
    }

    public void setTopPane(TopPane_Vertical top) {
        m_TopPane = top;
    }


    public void showBoardEditing() {
        LayoutInflater layoutinflater = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View window = layoutinflater.inflate(R.layout.board_editing_controls_vertical, null);
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

        m_LeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneLeft();
            }
        });

        m_RightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneRight();
            }
        });

        m_UpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneUp();
            }
        });

        m_DownButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.movePlaneDown();
            }
        });

        m_RotateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TopPane.rotatePlane();
            }
        });

        m_DoneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                init(GameStages.Game);
            }
        });
    }

    public void showGame() {
        LayoutInflater layoutinflater = (LayoutInflater) m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View window = layoutinflater.inflate(R.layout.game_stats_vertical, null);
        setRowCount(1);
        setColumnCount(1);
        GridLayout.LayoutParams paramsWindow = new GridLayout.LayoutParams(GridLayout.spec(0, 1), GridLayout.spec(0, 1));
        paramsWindow.setGravity(Gravity.CENTER);
        paramsWindow.setMargins(0, 0, 0,0);
        addView(window, paramsWindow);
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

    private TopPane_Vertical m_TopPane;
}
