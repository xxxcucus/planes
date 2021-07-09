package com.planes.android;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridLayout;

import java.util.HashMap;
import java.util.Map;


public class GameBoard extends GridLayout {
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

    public GameBoard(Context context) {
        super(context);
        m_Context = context;
        init(m_Context);
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Context = context;
        init(m_Context);
    }

    public GameBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_Context = context;
        init(m_Context);
    }

    public void setGameSettings(PlanesRoundInterface planeRound, boolean isTablet) {
        m_PlaneRound = planeRound;
        m_GRows = m_PlaneRound.getRowNo();
        m_GCols = m_PlaneRound.getColNo();
        m_PlaneNo = m_PlaneRound.getPlaneNo();
        m_ColorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneNo;
        m_Tablet = isTablet;
        //init(m_Context);
        updateBoards();
    }

    /**
     * Repositioning of grid squares inside the game board when the layout changes
     * (e.g. when going from one game stage to the other)
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();

        // These are the top and bottom edges in which we are performing layout.
        int topPos = getPaddingTop();
        int bottomPos = bottom - top - getPaddingBottom();

        int spacing = 0;

        final int newWidth = (Math.min(bottomPos - topPos, rightPos - leftPos) - spacing )/ (m_GRows + 2 * m_Padding);
        m_GridSquareSize = newWidth;

        int verticalOffset = 0;
        int horizontalOffset = 0;

        if (bottomPos - topPos > rightPos - leftPos) {
            verticalOffset = (bottomPos - topPos - rightPos + leftPos) / 2;
        } else {
            horizontalOffset = (rightPos - leftPos - bottomPos + topPos) / 2;
        }

        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            GridSquare child = (GridSquare) getChildAt(i);
            child.setWidth(newWidth);
            //Log.d("Planes", "Set width " + i);

            int childLeft = leftPos + horizontalOffset + spacing / 2 + child.getColNo() * newWidth;
            int childTop = topPos + verticalOffset + spacing / 2 + child.getRowNo() * newWidth;
            int childRight = leftPos + horizontalOffset +  spacing / 2 + child.getColNo() * newWidth + newWidth;
            int childBottom = topPos + verticalOffset + spacing / 2 + child.getRowNo() * newWidth + newWidth;

            child.layout(childLeft, childTop, childRight, childBottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //TODO: is this correct ?
        if (m_GridSquareSize == 0)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        else
            setMeasuredDimension((m_GRows + 2 * m_Padding) * m_GridSquareSize, (m_GRows + 2 * m_Padding) * m_GridSquareSize);
    }

    private void init(Context context) {
        
        int gridSize = 1;
        setRowCount(m_GRows + 2 * m_Padding);
        setColumnCount(m_GCols + 2 * m_Padding);
        m_GridSquares = new HashMap<PositionBoardPane, GridSquare>();

        for (int i = 0; i < m_GRows + 2 * m_Padding; ++i) {
            for (int j = 0; j < m_GCols + 2 * m_Padding; ++j) {
                GridSquare gs = new GridSquare(context, gridSize);
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
            count = m_PlaneRound.getPlayerGuessesNo();
        else
            count = m_PlaneRound.getComputerGuessesNo();

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
            //System.out.println("Guess type " + type);
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

        if (!m_IsComputer || (m_IsComputer && m_CurStage == GameStages.GameNotStarted)) {
        //if (true) {
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
        return squareColor;
    }


    public void touchEventUp(int row, int col, int row_diff, int col_diff) {
        if (row_diff == 0 && col_diff == 0) {
            touchInASingleSquare(row, col);
        } else {
            touchInMoreSquares(row, col, row + row_diff, col + col_diff);
        }
    }

    //simple touch
    private void touchInASingleSquare(int row, int col) {
        if (!m_IsComputer && m_CurStage == GameStages.BoardEditing) {
            int type = m_PlaneRound.getPlaneSquareType(row - m_Padding, col - m_Padding, m_IsComputer ? 1 : 0);
            if (type > 0)
                m_Selected = type - 1;
            updateBoards();
        }

        if (m_IsComputer && m_CurStage == GameStages.Game) {
            if (m_IsComputer) {
                System.out.println("Player guess");

                if (m_PlaneRound.playerGuessAlreadyMade(col - m_Padding, row - m_Padding) != 0) {
                    System.out.println("Player guess already made");
                    return;
                }

                m_PlaneRound.playerGuess(col - m_Padding, row - m_Padding);

                //check if the round ended
                if (m_PlaneRound.playerGuess_RoundEnds()) {
                    System.out.println("Round ends!");
                    String winnerText = m_PlaneRound.playerGuess_IsPlayerWinner() ? "Player wins !" : "Computer wins !";
                    if (m_PlaneRound.playerGuess_IsDraw())
                        winnerText = "Draw !";

                    if (m_Tablet) {
                        m_SiblingBoard.setNewRoundStage(false);
                        setNewRoundStage(false);
                    } else {
                        setNewRoundStage(true);
                    }
                    m_PlaneRound.roundEnds();
                    m_GameControls.roundEnds(!m_PlaneRound.playerGuess_IsPlayerWinner(), m_PlaneRound.playerGuess_IsDraw());
                } else {
                    if (!m_Tablet)
                        m_GameControls.updateStats(m_IsComputer);
                }
                updateBoards();
                if (m_SiblingBoard != null)
                    m_SiblingBoard.updateBoards();
            }
        }
    }

    //drag
    private void touchInMoreSquares(int row_first, int col_first, int row_last, int col_last) {
        //System.out.println("Drag from " + Integer.toString(row_first) + ", " + Integer.toString(col_first) + " to " + Integer.toString(row_last) + " , " + Integer.toString(col_last));
        if (!m_IsComputer && m_CurStage == GameStages.BoardEditing) {
            if (Math.abs(row_last - row_first) > Math.abs(col_last - col_first)) {
                //vertical movement
                if (row_last > row_first)
                    movePlaneRight();
                else
                    movePlaneLeft();
            } else {
                //horizontal movement
                if (col_last > col_first)
                    movePlaneDown();
                else
                    movePlaneUp();
            }
        }
    }

    public void movePlaneLeft() {
        boolean valid = m_PlaneRound.movePlaneLeft(m_Selected) == 1 ? true : false;
        updateBoards();
        m_GameControls.setDoneEnabled(valid);
    }

    public void movePlaneRight() {
        boolean valid = m_PlaneRound.movePlaneRight(m_Selected) == 1 ? true : false;
        updateBoards();
        m_GameControls.setDoneEnabled(valid);
    }

    public void movePlaneUp() {
        boolean valid = m_PlaneRound.movePlaneUpwards(m_Selected) == 1 ? true : false;
        updateBoards();
        m_GameControls.setDoneEnabled(valid);
    }

    public void movePlaneDown() {
        boolean valid = m_PlaneRound.movePlaneDownwards(m_Selected) == 1 ? true : false;
        updateBoards();
        m_GameControls.setDoneEnabled(valid);
    }

    public void rotatePlane() {
        boolean valid = m_PlaneRound.rotatePlane(m_Selected) == 1 ? true : false;
        updateBoards();
        m_GameControls.setDoneEnabled(valid);
    }

    public void setPlayerBoard() {
        m_IsComputer = false;
        updateBoards();
    }

    public void setComputerBoard() {
        m_IsComputer = true;
        updateBoards();
    }

    /**
     * Sets the game stage. If the boolean setRole is true, automatically transform to computer board.
     * setRole is true for phone devices.
     * @param setRole
     */
    public void setGameStage(boolean setRole) {
        m_CurStage = GameStages.Game;
        if (setRole)
            m_IsComputer = true;
        updateBoards();
    }

    public GameStages getGameStage() {
        return m_CurStage;
    }

    /**
     * Sets the board editing stage. If the boolean setRole is true, automatically transform to computer board.
     * setRole is true for phone devices.
     * @param setRole
     */
    public void setBoardEditingStage(boolean setRole) {
        m_CurStage = GameStages.BoardEditing;
        if (setRole)
            m_IsComputer = false;
        updateBoards();
    }

    /**
     * Sets the new round stage. If the boolean setRole is true, automatically transform to computer board.
     * setRole is true for phone devices.
     * @param setRole
     */
    public void setNewRoundStage(boolean setRole) {
        m_CurStage = GameStages.GameNotStarted;
        if (setRole)
            m_IsComputer = true;
        updateBoards();
    }

    public void setGameControls(GameControlsAdapter gameControls) {
        m_GameControls = gameControls;
    }

    public void setSiblingBoard(GameBoard siblingBoard) {
        m_SiblingBoard = siblingBoard;
    }

    public boolean isPlayer() {
        return !m_IsComputer;
    }

    private Map<PositionBoardPane, GridSquare> m_GridSquares;
    private PlanesRoundInterface m_PlaneRound;
    private int m_Padding = 0;
    private boolean m_IsComputer = false;
    private int m_MinPlaneBodyColor = 0;
    private int m_MaxPlaneBodyColor = 200;
    private GameStages m_CurStage = GameStages.BoardEditing;
    private int m_SelectedPlane = 0;

    private int m_GRows = 10;
    private int m_GCols = 10;
    private int m_PlaneNo = 3;
    private int m_ColorStep = 50;

    private Context m_Context;

    private int m_Selected = 0;
    private GameControlsAdapter m_GameControls = null;
    private boolean m_Tablet = false;
    private GameBoard m_SiblingBoard = null;
    private int m_GridSquareSize = 0;
}
