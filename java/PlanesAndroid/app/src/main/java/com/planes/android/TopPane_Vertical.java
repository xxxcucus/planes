package com.planes.android;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;

import com.planes.javafx.PlaneRoundJavaFx;

import java.util.HashMap;
import java.util.Map;


//Top pane in a vertical layout
public class TopPane_Vertical extends GridLayout {
    class PositionBoardPane {
        private int x = 0;
        private int y = 0;
        public PositionBoardPane(int i, int j) {
            x = i;
            y = j;
        }

        @Override
        public boolean equals(final Object other) {
            if (other == null)
                return false;
            if (this == other)
                return true;
            if (this.getClass() != other.getClass())
                return false;

            final PositionBoardPane pos = (PositionBoardPane)other;
            return this.x == pos.x && this.y == pos.y;
        }

        @Override
        public int hashCode() {
            return 100 * x + y;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }
    }

    public enum GameStages {
        GameNotStarted, BoardEditing, Game
    }

    public TopPane_Vertical(Context context) {
        super(context);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    public TopPane_Vertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    public TopPane_Vertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setGameSettings(nrows, ncols, nplanes);
        //m_PlaneRound = planeRound;
        m_Context = context;
    }

    public void setGameSettings(PlaneRoundJavaFx planeRound) {
        m_PlaneRound = planeRound;
        m_GRows = m_PlaneRound.getRowNo();
        m_GCols = m_PlaneRound.getColNo();
        m_PlaneNo = m_PlaneRound.getPlaneNo();
        m_PlaneRound = planeRound;
        m_ColorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneNo;
        init(m_Context);
        updateBoards();
    }

    private void init(Context context) {
        setRowCount(m_GRows + 2 * m_Padding);
        setColumnCount(m_GCols + 2 * m_Padding);
        m_GridSquares = new HashMap<PositionBoardPane, GridSquare>();

        for (int i = 0; i < m_GRows + 2 * m_Padding; ++i) {
            for (int j = 0; j < m_GCols + 2 * m_Padding; ++j) {
                GridSquare gs = new GridSquare(context);
                if ( i < m_Padding || i >= m_GRows + m_Padding || j < m_Padding || j >= m_GCols + m_Padding)
                    gs.setBackgroundColor(Color.YELLOW);
                else
                    gs.setBackgroundColor(getResources().getColor(R.color.aqua));
                gs.setGuess(-1);
                gs.setRowCount(m_GRows + 2 * m_Padding);
                gs.setColCount(m_GCols + 2 * m_Padding);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i, 1), GridLayout.spec(j, 1));
                addView(gs, params);
                PositionBoardPane position = new PositionBoardPane(i, j);
                m_GridSquares.put(position, gs);
            }
        }
    }

    public void updateBoards() {
        //draw the squares background
        for (int i = 0; i < m_GRows + 2 * m_Padding; i++) {
            for (int j = 0; j < m_GCols + 2 * m_Padding; j++) {
                GridSquare c = m_GridSquares.get(new PositionBoardPane(i, j));
                c.setBackgroundColor(computeSquareBackgroundColor(i, j));
           }
        } //display background of square; double for loop
    }

    public int computeSquareBackgroundColor(int i, int j) {
        int squareColor = 0;

        if (i < m_Padding || i >= m_GRows + m_Padding || j < m_Padding || j >= m_GCols + m_Padding) {
            squareColor = Color.YELLOW;
        } else {
            squareColor = getResources().getColor(R.color.aqua);
        }

        //if (!m_IsComputer || (m_IsComputer && m_CurStage == GameStages.GameNotStarted)) {
        if (true) {
            int type = m_PlaneRound.getPlaneSquareType(i - m_Padding, j - m_Padding, m_IsComputer ? 1 : 0);
            switch (type) {
                //intersecting planes
                case -1:
                    squareColor = Color.RED;
                    break;
                //plane head
                case -2:
                    squareColor = Color.GREEN;
                    break;
                //not a plane
                case 0:
                    break;
                //plane but not plane head
                default:
                    if ((type - 1) == m_Selected) {
                        squareColor = Color.BLUE;
                    } else {
                        int grayCol = m_MinPlaneBodyColor + type * m_ColorStep;
                        squareColor = Color.rgb(grayCol, grayCol, grayCol);
                    }
                    break;
            }
        }
        //System.out.println(Integer.toString(i) + ", " + Integer.toString(j) + "= " + Integer.toString(squareColor));
        return squareColor;
    }


    private Map<PositionBoardPane, GridSquare> m_GridSquares;
    private PlaneRoundJavaFx m_PlaneRound;
    private int m_Padding = 0;
    private boolean m_IsComputer = false;
    private int m_MinPlaneBodyColor = 0;
    private int m_MaxPlaneBodyColor = 200;
    private GameStages m_CurStage = GameStages.BoardEditing;
    private int m_SelectedPlane = 0;
    //private EventHandler<MouseEvent> m_ClickedHandler;

    private int m_GRows = 10;
    private int m_GCols = 10;
    private int m_PlaneNo = 3;
    private int m_ColorStep = 50;

    private Context m_Context;

    private int m_Selected = 0;
}
