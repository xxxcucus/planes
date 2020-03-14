package com.planes.common;

import com.planes.android.PlanesRoundInterface;

public class PlanesRoundJava implements PlanesRoundInterface {

    //creates the PlaneRound object in the game engine
    //must be called a single time
    public void createPlanesRound() {

    }

    //show the planes
    public int getRowNo() {
        return 0;
    }
    public int getColNo() {
        return 0;
    }
    public int getPlaneNo() {
        return 0;
    }
    public int getPlaneSquareType(int i, int j, int isComputer) {
        return 0;
    }

    //edit the board
    public int movePlaneLeft(int idx) {
        return 0;
    }
    public int movePlaneRight(int idx) {
        return 0;
    }
    public int movePlaneUpwards(int idx) {
        return 0;
    }
    public int movePlaneDownwards(int idx) {
        return 0;
    }
    public int rotatePlane(int idx) {
        return 0;
    }
    public void doneClicked() {

    }

    //play the game
    public int playerGuessAlreadyMade(int row, int col) {
        return 0;
    }
    public void playerGuess(int row, int col) {

    }

    public boolean playerGuess_RoundEnds() {
        return false;
    }

    public boolean playerGuess_IsPlayerWinner() {
        return false;
    }

    public boolean playerGuess_ComputerMoveGenerated() {
        return false;
    }

    public int playerGuess_StatNoPlayerMoves() {
        return 0;
    }

    public int playerGuess_StatNoPlayerHits() {
        return 0;
    }
    public int playerGuess_StatNoPlayerMisses() {
        return 0;
    }
    public int playerGuess_StatNoPlayerDead() {
        return 0;
    }
    public int playerGuess_StatNoPlayerWins() {
        return 0;
    }
    public int playerGuess_StatNoComputerMoves() {
        return 0;
    }
    public int playerGuess_StatNoComputerHits() {
        return 0;
    }
    public int playerGuess_StatNoComputerMisses() {
        return 0;
    }
    public int playerGuess_StatNoComputerDead() {
        return 0;
    }
    public int playerGuess_StatNoComputerWins() {
        return 0;
    }

    public void roundEnds() {

    }
    public void initRound() {

    }

    //show the guesses
    public int getPlayerGuessesNo() {
        return 0;
    }
    public int getPlayerGuessRow(int idx) {
        return 0;
    }
    public int getPlayerGuessCol(int idx) {
        return 0;
    }
    public int getPlayerGuessType(int idx) {
        return 0;
    }

    public int getComputerGuessesNo() {
        return 0;
    }
    public int getComputerGuessRow(int idx) {
        return 0;
    }
    public int getComputerGuessCol(int idx) {
        return 0;
    }
    public int getComputerGuessType(int idx) {
        return 0;
    }

    public int getGameStage() {
        return 0;
    }

}
