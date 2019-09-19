package com.planes.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.GridLayout;

import com.planes.javafx.PlaneRoundJavaFx;

import java.util.HashMap;
import java.util.Map;


public class GameBoard extends GridLayout {

    /**
     * Pair of ints used to index a grid square in the game board.
     */
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

    /**
     * Constructor method inherited from GridLayout
     * @param context
     */
    public GameBoard(Context context) {
        super(context);
        m_Context = context;
    }

    /**
     * Constructor method inherited from GridLayout
     * @param context
     */
    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Context = context;
    }

    /**
     * Constructor method inherited from GridLayout
     * @param context
     */
    public GameBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_Context = context;
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
        final int count = getChildCount();

        // These are the far left and right edges in which we are performing layout.
        int leftPos = getPaddingLeft();
        int rightPos = right - left - getPaddingRight();


        // These are the top and bottom edges in which we are performing layout.
        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        final int newWidth = Math.min(parentBottom - parentTop, rightPos - leftPos) / (m_GRows + 2 * m_Padding);

        for (int i = 0; i < count; i++) {
            GridSquare child = (GridSquare) getChildAt(i);
            child.setWidth(newWidth);
            //Log.d("Planes", "Set width " + i);
            child.layout(leftPos + child.getColNo() * newWidth, parentTop + child.getRowNo() * newWidth,
                    leftPos + child.getColNo() * newWidth + newWidth,  parentTop + child.getRowNo() * newWidth + newWidth);
        }
    }

    /**
     * Save a reference to the game controller object.
     * Initialize member variables that require the controller object.
     * Create the grid squares.
     * @param planeRound
     * @param isTablet
     */
    public void setGameSettings(PlaneRoundJavaFx planeRound, boolean isTablet) {
        m_PlaneRound = planeRound;
        m_GRows = m_PlaneRound.getRowNo();
        m_GCols = m_PlaneRound.getColNo();
        m_PlaneNo = m_PlaneRound.getPlaneNo();
        m_PlaneRound = planeRound;
        m_ColorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneNo;
        init(m_Context, isTablet);
        updateBoards();
    }

    public void setPlaneRound(PlaneRoundJavaFx planeRound) {
        m_PlaneRound = planeRound;
    }

    /**
     *
     * @param context
     * @param isTablet
     */

    /**
     * Depending of device orientation and type it computes the gameboard square size in pixels.
     * It initializes the individual GridSquare objects that make the GameBoard and groups them
     * inside the HashMap called m_GridSquares.
     * @param context
     * @param isTablet
     */

    private void init(Context context, boolean isTablet) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        int statusBarHeight = 0;
        int resource = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resource > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resource);
        }

        int longDim = Math.max(height - actionBarHeight - statusBarHeight, width);
        int shortDim = Math.min(height - actionBarHeight - statusBarHeight, width);

        int gridSize = 0;

        if (!isTablet) {
            gridSize = shortDim / (m_GRows + 2 * m_Padding);
        } else {
            gridSize = Math.min(longDim / 2, shortDim) / (m_GRows + 2 * m_Padding);
        }

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


    /**
     * Read the position of the planes from the game controller as well as
     * the guesses and display them on the game board.
     */
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

        System.out.println((m_IsComputer ? "Computer board" : "Player board")  + count + " guesses");

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

    /**
     * For the given row and column on the game board give back the color of the
     * grid square
     * @param i - row
     * @param j - column
     * @return - computed color
     */
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
        //System.out.println(Integer.toString(i) + ", " + Integer.toString(j) + "= " + Integer.toString(squareColor));
        return squareColor;
    }

    /**
     * React to touch events in the game
     * @param row
     * @param col
     */
    public void touchEvent(int row, int col) {
        System.out.println("Touch event" + row + " " + col);
        System.out.println("IsComputer " + m_IsComputer);

        String stageString = null;

        switch(m_CurStage) {
            case BoardEditing:
                stageString = "BoardEditing";
                break;
            case Game:
                stageString = "Game";
                break;
            case GameNotStarted:
                stageString = "GameNotStarted";
                break;
        }

        System.out.println("Game stage " + stageString);

        //if it is player board and board editing change the selected plane when clicked on a plane
        if (!m_IsComputer && m_CurStage == GameStages.BoardEditing) {
            int type = m_PlaneRound.getPlaneSquareType(row - m_Padding, col - m_Padding, m_IsComputer ? 1 : 0);
            if (type > 0)
                m_Selected = type - 1;
            updateBoards();
        }
        //if it is computer board and game then it is a guessing attempt
        if (m_IsComputer && m_CurStage == GameStages.Game) {
            if (m_IsComputer) {
                System.out.println("Player guess");

                if (m_PlaneRound.playerGuessAlreadyMade(col - m_Padding, row - m_Padding) != 0) {
                    System.out.println("Player guess already made");
                    return;
                }

                m_PlaneRound.playerGuess(col - m_Padding, row - m_Padding);

                //update the statistics
                int playerWins = m_PlaneRound.playerGuess_StatNoPlayerWins();
                int computerWins = m_PlaneRound.playerGuess_StatNoComputerWins();


                //check if the round ended
                if (m_PlaneRound.playerGuess_RoundEnds()) {
                    System.out.println("Round ends!");
                    String winnerText = m_PlaneRound.playerGuess_IsPlayerWinner() ? "Computer wins !" : "Player wins !";
                    //announceRoundWinner(winnerText);
                    m_CurStage = GameStages.GameNotStarted;
                    m_PlaneRound.roundEnds();
                    m_BoardControls.roundEnds(playerWins, computerWins, m_PlaneRound.playerGuess_IsPlayerWinner());
                } else {
                    m_BoardControls.updateStats(m_IsComputer);
                }
                updateBoards();
                if (m_PairBoard != null)
                    m_PairBoard.updateBoards();
            }
        }
    }

    public void movePlaneLeft() {
        boolean valid = m_PlaneRound.movePlaneLeft(m_Selected) == 1 ? true : false;
        updateBoards();
        m_BoardControls.setDoneEnabled(valid);
    }

    public void movePlaneRight() {
        boolean valid = m_PlaneRound.movePlaneRight(m_Selected) == 1 ? true : false;
        updateBoards();
        m_BoardControls.setDoneEnabled(valid);
    }

    public void movePlaneUp() {
        boolean valid = m_PlaneRound.movePlaneUpwards(m_Selected) == 1 ? true : false;
        updateBoards();
        m_BoardControls.setDoneEnabled(valid);
    }

    public void movePlaneDown() {
        boolean valid = m_PlaneRound.movePlaneDownwards(m_Selected) == 1 ? true : false;
        updateBoards();
        m_BoardControls.setDoneEnabled(valid);
    }

    public void rotatePlane() {
        boolean valid = m_PlaneRound.rotatePlane(m_Selected) == 1 ? true : false;
        updateBoards();
        m_BoardControls.setDoneEnabled(valid);
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

    public void setBoardControls(ControlWidgetsAdaptor controls) {
        m_BoardControls = controls;
    }

    public void setPairBoard(GameBoard board) {
        m_PairBoard = board;
    }

    /**
     * Container for squares on the game board
     */
    private Map<PositionBoardPane, GridSquare> m_GridSquares;
    /**
     * Game controller object
     */
    private PlaneRoundJavaFx m_PlaneRound;
    /**
     * how many rows are added to the game board in order to display plane rotation
     */
    private int m_Padding = 0;
    /**
     * does the board belong to computer or player ?
     */
    private boolean m_IsComputer = false;
    /**
     * Grey color used for the darkest plane
     */
    private int m_MinPlaneBodyColor = 0;
    /**
     * Grey color used for the less dark plane
     */
    private int m_MaxPlaneBodyColor = 200;
    /**
     * The current game stage.
     */
    private GameStages m_CurStage = GameStages.BoardEditing;
    /**
     * The currently selected plane when editing the board.
     */
    private int m_SelectedPlane = 0;
    //private EventHandler<MouseEvent> m_ClickedHandler;

    private int m_GRows = 10;
    private int m_GCols = 10;
    private int m_PlaneNo = 3;
    private int m_ColorStep = 50;

    private Context m_Context;

    /**
     * Which plane is selected in board editing mode.
     */
    private int m_Selected = 0;
    /**
     * Reference to the adaptor of controls.
     */
    private ControlWidgetsAdaptor m_BoardControls = null;
    /**
     * If the device is a tablet, reference to the other board.
     */
    private GameBoard m_PairBoard = null;
}
