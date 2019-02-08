package com.planes.android;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;

import com.planes.javafx.PlaneRoundJavaFx;

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

    public void setGameSettings(int nrows, int ncols, int nplanes) {
        m_GRows = nrows;
        m_GCols = ncols;
        m_PlaneNo = nplanes;
        init(m_Context);
    }

    private void init(Context context) {
        setRowCount(m_GRows + 2 * m_Padding);
        setColumnCount(m_GCols + 2 * m_Padding);

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
            }
        }
    }


    private Map<PositionBoardPane, GridSquare> m_GridSquares;
    //private PlaneRoundJavaFx m_PlaneRound;
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

    private PlaneRoundJavaFx m_PlaneRound;

    private Context m_Context;
}
