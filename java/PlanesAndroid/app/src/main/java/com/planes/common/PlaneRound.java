package com.planes.common;
import android.support.v4.util.Pair;
import java.util.Vector;

public class PlaneRound {

    public PlaneRound(int rowNo, int colNo, int planeNo) {
        m_rowNo = rowNo;
        m_colNo = colNo;
        m_planeNo = planeNo;

        //builds the plane grid objects
        m_PlayerGrid = new PlaneGrid(m_rowNo, m_colNo, m_planeNo, false);
        m_ComputerGrid = new PlaneGrid(m_rowNo, m_colNo, m_planeNo, true);

        //builds the computer logic object
        m_computerLogic = new ComputerLogic(m_rowNo, m_colNo, m_planeNo);

        m_computerGuessList = new Vector<GuessPoint>();
        m_playerGuessList = new Vector<GuessPoint>();
        m_gameStats = new GameStatistics();

        reset();
        initRound();
    }

    //inits a new round
    public void initRound() {
        m_PlayerGrid.initGrid();
        m_ComputerGrid.initGrid();
        m_State = GameStages.BoardEditing;
        m_isComputerFirst = !m_isComputerFirst;
        m_playerGuessList.clear();
        m_computerGuessList.clear();

        m_gameStats.reset();
        m_computerLogic.reset();
    }
    //switches to the state GameNotStarted
    public void setRoundEnd() {
        m_State = GameStages.GameNotStarted;
    }

    //checks if we are in state GameNotStarted
    public boolean didRoundEnd() {
        return m_State == GameStages.GameNotStarted;
    }

    /**
     @param[in] gp - the player's guess together with its evaluation
     @param[out] pgr - response to the player's guess: the computer's guess, if the game ended, winner, game statistics
     Plays a step in the game, as triggered by the player's guess gp.
     */
    public PlayerGuessReaction playerGuess(final GuessPoint gp) {
        PlayerGuessReaction pgr = new PlayerGuessReaction();

        if (m_State != GameStages.Game)
            return pgr;

        if (m_isComputerFirst) {
            GuessPoint gpc = guessComputerMove();
            updateGameStats(gpc, true);
            pgr.m_ComputerMoveGenerated = true;
            pgr.m_ComputerGuess = gpc;

            //update the game statistics
            updateGameStats(gp, false);
            //add the player's guess to the list of guesses
            //assume that the guess is different from the other guesses
            m_playerGuessList.add((GuessPoint)gp.clone());
        } else {
            //update the game statistics
            updateGameStats(gp, false);
            //add the player's guess to the list of guesses
            //assume that the guess is different from the other guesses
            m_playerGuessList.add((GuessPoint)gp.clone());

            GuessPoint gpc = guessComputerMove();
            updateGameStats(gpc, true);
            pgr.m_ComputerMoveGenerated = true;
            pgr.m_ComputerGuess = gpc;
        }

        Pair<Boolean, Boolean> roundEndsResult = roundEnds();
        if (roundEndsResult.first) {
            m_gameStats.updateWins(!roundEndsResult.second);
            pgr.m_RoundEnds = true;
            m_State = GameStages.GameNotStarted;
            pgr.m_isPlayerWinner = roundEndsResult.second;
        } else {
            pgr.m_RoundEnds = false;
        }

        pgr.m_GameStats = m_gameStats;
        return pgr;
    }

    /**
     @param[in] row, col - coordinates of player's guess
     Check if a guess was already made at this position.
     */
    public int playerGuessAlreadyMade(int row, int col) {
        Coordinate2D qp = new Coordinate2D(col, row);
        for (GuessPoint guess : m_playerGuessList) {
            if (guess.m_row == col && guess.m_col == row)
                return 1;
        }
        return 0;
    }


    /**
     @param[in] row, col - coordinates of player's guess
     @param[out] guessRes - the evaluation of the player's guess
     @param[out] pgr - response to the player's guess: the computer's guess, if the game ended, winner, game statistics
     Plays a step in the game, as triggered by the the player's guess coordinates.
     */
    public Pair<Type, PlayerGuessReaction> playerGuessIncomplete(int row, int col) {
        Coordinate2D qp = new Coordinate2D(col, row);
        Type guessRes = m_ComputerGrid.getGuessResult(qp);
        GuessPoint gp = new GuessPoint(qp.x(), qp.y(), guessRes);
        PlayerGuessReaction pgr = playerGuess(gp);
        return new Pair<Type, PlayerGuessReaction>(guessRes, pgr);
    }

    /**
     Rotate the plane and return false if the current plane configuration is valid.
     */
    public boolean rotatePlane(int idx) {
        m_PlayerGrid.rotatePlane(idx);
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid());
    }
    /**
     Move the plane left and return false if the current plane configuration is valid.
     */
    public boolean movePlaneLeft(int idx) {
        m_PlayerGrid.movePlaneLeft(idx);
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid());
    }
    /**
     Move the plane right and return false if the current plane configuration is valid.
     */
    public boolean movePlaneRight(int idx) {
        m_PlayerGrid.movePlaneRight(idx);
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid());
    }

    /**
     Move the plane upwards and return false if the current plane configuration is valid.
     */
    public boolean movePlaneUpwards(int idx) {
        m_PlayerGrid.movePlaneUpwards(idx);
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid());
    }
    /**
     Move the plane downwards and return false if the current plane configuration is valid.
     */
    public boolean movePlaneDownwards(int idx) {
        m_PlayerGrid.movePlaneDownwards(idx);
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid());
    }

    public void doneEditing() {
        m_State = GameStages.Game;
    }

    public PlaneGrid playerGrid() { return m_PlayerGrid; }
    public PlaneGrid computerGrid() { return m_ComputerGrid; }
    public ComputerLogic computerLogic() { return m_computerLogic; }

    public int getRowNo() {
        return m_rowNo;
    }

    public int getColNo() {
        return m_colNo;
    }

    public int getPlaneNo() {
        return m_planeNo;
    }

    /*
	-2 - plane head
	-1 - plane intersection
	0 - is not on plane
	i - plane but not head
    */
    public int getPlaneSquareType(int row, int col, boolean isComputer) {
        Pair<Boolean, Integer> isOnPlane = Pair.create(false, 0);

        if (isComputer) {
            isOnPlane = m_ComputerGrid.isPointOnPlane(row, col);
            if (!isOnPlane.first)
                return 0;
            int annotation = m_ComputerGrid.getPlanePointAnnotation(isOnPlane.second);
            Vector<Integer> planesIdx = m_ComputerGrid.decodeAnnotation(annotation);
            if (planesIdx.size() > 1) {
                return -1;
            }

            if (planesIdx.size() == 1) {
                if (planesIdx.get(0) < 0)
                    return -2;
                else
                    return (planesIdx.get(0) + 1);
            }
        } else {
            isOnPlane = m_PlayerGrid.isPointOnPlane(row, col);
            if (!isOnPlane.first)
                return 0;
            int annotation = m_PlayerGrid.getPlanePointAnnotation(isOnPlane.second);
            Vector<Integer> planesIdx = m_PlayerGrid.decodeAnnotation(annotation);
            if (planesIdx.size() > 1) {
                return -1;
            }

            if (planesIdx.size() == 1) {
                if (planesIdx.get(0) < 0)
                    return -2;
                else
                    return (planesIdx.get(0) + 1);
            }
        }

        return 0;
    }

    public int getPlayerGuessesNo() {
        return m_playerGuessList.size();
    }

    public int getComputerGuessesNo() {
        return m_computerGuessList.size();
    }

    public GuessPoint getPlayerGuess(int idx) {
        if (idx < 0 || idx >= m_playerGuessList.size())
            return new GuessPoint(-1, -1, Type.Miss);
		else
            return (GuessPoint)m_playerGuessList.get(idx).clone();
    }

    public GuessPoint getComputerGuess(int idx) {
        if (idx < 0 || idx >= m_computerGuessList.size())
            return new GuessPoint(-1, -1, Type.Miss);
		else
            return (GuessPoint)m_computerGuessList.get(idx).clone();
    }

    public int getCurrentStage() {
        return m_State.getValue();
    }

    //update game statistics
    private void updateGameStats(final GuessPoint gp, boolean isComputer) {
        m_gameStats.updateStats(gp, isComputer);
    }
    //tests whether all of the planes have been guessed
    private boolean enoughGuesses(PlaneGrid pg, final Vector<GuessPoint> guessList) {

        int count = 0;

        for (int i = 0; i < guessList.size(); i++) {
            GuessPoint gp = guessList.get(i);
            if (gp.m_type == Type.Dead)
                count++;
        }

        return (count >= pg.getPlaneNo());
    }
    //based on the available information makes the next move for the computer
    private GuessPoint guessComputerMove() {
        //use the computer strategy to get a move
        Pair<Boolean, Coordinate2D> p = m_computerLogic.makeChoice();

        //use the player grid to see the result of the grid
        Type tp = m_PlayerGrid.getGuessResult(p.second);
        GuessPoint gp = new GuessPoint(p.second.x(), p.second.y(), tp);

        //add the data to the computer strategy
        m_computerLogic.addData(gp);

        //update the computer guess list
        m_computerGuessList.add(gp);

        return gp;
    }
    //resets the round
    private void reset() {
        m_PlayerGrid.resetGrid();
        m_ComputerGrid.resetGrid();

        m_playerGuessList.clear();
        m_computerGuessList.clear();

        m_gameStats.reset();
        m_computerLogic.reset();
    }
    //check to see if there is a winner
    private Pair<Boolean, Boolean> roundEnds() {
        //at equal scores computer wins
        boolean isPlayerWinner = false;

        boolean computerFinished = enoughGuesses(m_PlayerGrid, m_computerGuessList);
        boolean playerFinished = enoughGuesses(m_ComputerGrid, m_playerGuessList);

        if (!computerFinished && playerFinished)
            isPlayerWinner = true;

        return Pair.create((playerFinished || computerFinished), isPlayerWinner);
    }



    //whether the computer or the player moves first
    private boolean m_isComputerFirst = false;
    //the  game statistics
    private GameStatistics m_gameStats;

    //the player and computer's grid
    private PlaneGrid m_PlayerGrid;
    private PlaneGrid m_ComputerGrid;

    //the list of guesses for computer and player
    private Vector<GuessPoint> m_computerGuessList;
    private Vector<GuessPoint> m_playerGuessList;

    //the computer's strategy
    private ComputerLogic m_computerLogic;

    private GameStages m_State = GameStages.GameNotStarted;

    //size of the grid and number of planes
    private int m_rowNo = 10;
    private int m_colNo = 10;
    private int m_planeNo = 3;
}
