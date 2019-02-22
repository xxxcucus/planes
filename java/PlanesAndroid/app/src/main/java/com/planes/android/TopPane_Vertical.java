package com.planes.android;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridLayout;

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
                gs.setRow(i);
                gs.setColumn(j);
                gs.setParent(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(j, 1), GridLayout.spec(i, 1));
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
                c.setGuess(-1);
                c.setBackgroundColor(computeSquareBackgroundColor(i, j));
                c.invalidate();
           }
        } //display background of square; double for loop

        int count = 0;

        if (m_IsComputer)
            count = m_PlaneRound.getComputerGuessesNo();
        else
            count = m_PlaneRound.getPlayerGuessesNo();

        System.out.println("" + count + " guesses");

        for (int i = 0; i < count; i++) {
            int row = 0;
            int col = 0;
            int type = 0;

            if (m_IsComputer) {
                row = m_PlaneRound.getPlayerGuessRow(i);
                col = m_PlaneRound.getPlayerGuessCol(i);
                type = m_PlaneRound.getPlayerGuessType(i);
            } else {
                row = m_PlaneRound.getComputerGuessRow(i);
                col = m_PlaneRound.getComputerGuessCol(i);
                type = m_PlaneRound.getComputerGuessType(i);
            }

            GridSquare c = m_GridSquares.get(new PositionBoardPane(row + m_Padding, col + m_Padding));
            System.out.println("Guess type " + type);
            c.setGuess(type);
            c.invalidate();
        }
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
                    if ((type - 1) == m_Selected && !m_IsComputer && m_CurStage == GameStages.BoardEditing) {
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

    public void touchEvent(int row, int col) {
        System.out.println("Touch event" + row + " " + col);

        if (!m_IsComputer && m_CurStage == GameStages.BoardEditing) {
            int type = m_PlaneRound.getPlaneSquareType(row - m_Padding, col - m_Padding, m_IsComputer ? 1 : 0);
            if (type > 0)
                m_Selected = type - 1;
            updateBoards();
        }

        if (m_IsComputer && m_CurStage == GameStages.Game) {
            if (m_IsComputer) {
                System.out.println("Player guess");
                m_PlaneRound.playerGuess(col - m_Padding, row - m_Padding);
                m_BottomPane.updateStats(m_IsComputer);
                updateBoards();
            }
        }
    }

    public void movePlaneLeft() {
        m_PlaneRound.movePlaneLeft(m_Selected);
        updateBoards();
    }

    public void movePlaneRight() {
        m_PlaneRound.movePlaneRight(m_Selected);
        updateBoards();
    }

    public void movePlaneUp() {
        m_PlaneRound.movePlaneUpwards(m_Selected);
        updateBoards();
    }

    public void movePlaneDown() {
        m_PlaneRound.movePlaneDownwards(m_Selected);
        updateBoards();
    }

    public void rotatePlane() {
        m_PlaneRound.rotatePlane(m_Selected);
        updateBoards();
    }

    public void setPlayerBoard() {
        //if (m_IsComputer) {
            m_IsComputer = false;
            updateBoards();
        //}
    }

    public void setComputerBoard() {
        //if (!m_IsComputer) {
            m_IsComputer = true;
            updateBoards();
        //}
    }

    public void setGameStage() {
        m_CurStage = GameStages.Game;
        m_IsComputer = true;
        updateBoards();
    }

    public void setBottomPane(BottomPane_Vertical bottom) {
        m_BottomPane = bottom;
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
    private BottomPane_Vertical m_BottomPane;

}
