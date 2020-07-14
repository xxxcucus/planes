package com.planes.android;

//TODO: to move in common
public interface PlanesRoundInterface {

    //creates the PlaneRound object in the game engine
    //must be called a single time
    void createPlanesRound();

    //show the planes
    int getRowNo();
    int getColNo();
    int getPlaneNo();
    int getPlaneSquareType(int i, int j, int isComputer);

    //edit the board
    int movePlaneLeft(int idx);
    int movePlaneRight(int idx);
    int movePlaneUpwards(int idx);
    int movePlaneDownwards(int idx);
    int rotatePlane(int idx);
    void doneClicked();

    //play the game
    int playerGuessAlreadyMade(int row, int col);
    void playerGuess(int row, int col);
    boolean playerGuess_RoundEnds();
    boolean playerGuess_IsPlayerWinner();
    boolean playerGuess_IsDraw();
    boolean playerGuess_ComputerMoveGenerated();
    int playerGuess_StatNoPlayerMoves();
    int playerGuess_StatNoPlayerHits();
    int playerGuess_StatNoPlayerMisses();
    int playerGuess_StatNoPlayerDead();
    int playerGuess_StatNoPlayerWins();
    int playerGuess_StatNoComputerMoves();
    int playerGuess_StatNoComputerHits();
    int playerGuess_StatNoComputerMisses();
    int playerGuess_StatNoComputerDead();
    int playerGuess_StatNoComputerWins();
    int playerGuess_StatNoDraws();

    void roundEnds();
    void initRound();

    //show the guesses
    int getPlayerGuessesNo();
    int getPlayerGuessRow(int idx);
    int getPlayerGuessCol(int idx);
    int getPlayerGuessType(int idx);

    int getComputerGuessesNo();
    int getComputerGuessRow(int idx);
    int getComputerGuessCol(int idx);
    int getComputerGuessType(int idx);

    int getGameStage();
}
