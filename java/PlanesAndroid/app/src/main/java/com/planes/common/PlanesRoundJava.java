package com.planes.common;
import androidx.core.util.Pair;
import com.planes.android.PlanesRoundInterface;

public class PlanesRoundJava implements PlanesRoundInterface {

    public PlanesRoundJava() {}

    //creates the PlaneRound object in the game engine
    //must be called a single time
    public void createPlanesRound() {
        if (global_Round != null)
            return;

        global_Round = new PlaneRound(10, 10, 3);
        global_Round.initRound();
    }

    //show the planes
    public int getRowNo() {
        return global_Round.getRowNo();
    }
    public int getColNo() {
        return global_Round.getColNo();
    }
    public int getPlaneNo() {
        return global_Round.getPlaneNo();
    }
    public int getPlaneSquareType(int i, int j, int isComputer) {
        return global_Round.getPlaneSquareType(i, j, isComputer > 0 ? true : false);
    }

    //edit the board
    public int movePlaneLeft(int idx) {
        return global_Round.movePlaneLeft(idx) ? 1 : 0;
    }
    public int movePlaneRight(int idx) {
        return global_Round.movePlaneRight(idx) ? 1 : 0;
    }
    public int movePlaneUpwards(int idx) {
        return global_Round.movePlaneUpwards(idx) ? 1 : 0;
    }
    public int movePlaneDownwards(int idx) {
        return global_Round.movePlaneDownwards(idx) ? 1 : 0;
    }
    public int rotatePlane(int idx) {
        return global_Round.rotatePlane(idx) ? 1 : 0;
    }
    public void doneClicked() {
        global_Round.doneEditing();
    }

    //play the game
    public int playerGuessAlreadyMade(int row, int col) {
        return global_Round.playerGuessAlreadyMade(row, col);
    }
    public void playerGuess(int row, int col) {
        Pair<Type, PlayerGuessReaction> result = global_Round.playerGuessIncomplete(row, col);
        global_Guess_Result = result.first;
        global_Player_Guess_Reaction = result.second;
    }

    public boolean playerGuess_RoundEnds() {
        return global_Player_Guess_Reaction.m_RoundEnds;
    }

    public boolean playerGuess_IsDraw() {
        return global_Player_Guess_Reaction.m_IsDraw;
    }

    public boolean playerGuess_IsPlayerWinner() {
        return global_Player_Guess_Reaction.m_isPlayerWinner;
    }

    public boolean playerGuess_ComputerMoveGenerated() {
        return global_Player_Guess_Reaction.m_ComputerMoveGenerated;
    }

    public int playerGuess_StatNoPlayerMoves() {
        return global_Player_Guess_Reaction.m_GameStats.m_playerMoves;
    }

    public int playerGuess_StatNoPlayerHits() {
        return global_Player_Guess_Reaction.m_GameStats.m_playerHits;
    }
    public int playerGuess_StatNoPlayerMisses() {
        return global_Player_Guess_Reaction.m_GameStats.m_playerMisses;
    }
    public int playerGuess_StatNoPlayerDead() {
        return global_Player_Guess_Reaction.m_GameStats.m_playerDead;
    }
    public int playerGuess_StatNoPlayerWins() {
        return global_Player_Guess_Reaction.m_GameStats.m_playerWins;
    }
    public int playerGuess_StatNoComputerMoves() {
        return global_Player_Guess_Reaction.m_GameStats.m_computerMoves;
    }
    public int playerGuess_StatNoComputerHits() {
        return global_Player_Guess_Reaction.m_GameStats.m_computerHits;
    }
    public int playerGuess_StatNoComputerMisses() {
        return global_Player_Guess_Reaction.m_GameStats.m_computerMisses;
    }
    public int playerGuess_StatNoComputerDead() {
        return global_Player_Guess_Reaction.m_GameStats.m_computerDead;
    }
    public int playerGuess_StatNoComputerWins() {
        return global_Player_Guess_Reaction.m_GameStats.m_computerWins;
    }

    public int playerGuess_StatNoDraws() {
        return global_Player_Guess_Reaction.m_GameStats.m_draws;
    }

    public void roundEnds() {
        global_Round.setRoundEnd();
    }
    public void initRound() {
        global_Round.initRound();
        global_Player_Guess_Reaction.m_GameStats.reset();
    }

    //show the guesses
    public int getPlayerGuessesNo() {
        return global_Round.getPlayerGuessesNo();
    }
    public int getPlayerGuessRow(int idx) {
        return global_Round.getPlayerGuess(idx).m_row;
    }
    public int getPlayerGuessCol(int idx) {
        return global_Round.getPlayerGuess(idx).m_col;
    }
    public int getPlayerGuessType(int idx) {
        return global_Round.getPlayerGuess(idx).m_type.getValue();
    }

    public int getComputerGuessesNo() {
        return global_Round.getComputerGuessesNo();
    }
    public int getComputerGuessRow(int idx) {
        return global_Round.getComputerGuess(idx).m_row;
    }
    public int getComputerGuessCol(int idx) {
        return global_Round.getComputerGuess(idx).m_col;
    }
    public int getComputerGuessType(int idx) {
        return global_Round.getComputerGuess(idx).m_type.getValue();
    }

    public int getGameStage() {
        return global_Round.getCurrentStage();
    }

    public boolean setComputerSkill(int skill) {
        return global_Round.setComputerSkill(skill);
    }

    public boolean setShowPlaneAfterKill(boolean show) {
        return global_Round.setShowPlaneAfterKill(show);
    }

    public int getComputerSkill() {
        return global_Round.getComputerSkill();
    }

    public boolean getShowPlaneAfterKill() {
        return global_Round.getShowPlaneAfterKill();
    }

    private static PlaneRound global_Round = null;
    private static Type global_Guess_Result = Type.Miss;
    private static PlayerGuessReaction global_Player_Guess_Reaction = new PlayerGuessReaction();

}
