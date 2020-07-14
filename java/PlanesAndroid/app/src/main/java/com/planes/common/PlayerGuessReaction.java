package com.planes.common;

public class PlayerGuessReaction {

    //victory for computer or player
    public boolean m_RoundEnds = false;
    //is it a draw
    public boolean m_IsDraw = false;
    //who won
    public boolean m_isPlayerWinner = false;
    //if no victory then a computer move is generated
    public boolean m_ComputerMoveGenerated = false;
    //which computer move was generated
    public GuessPoint m_ComputerGuess;
    public GameStatistics m_GameStats;

    public PlayerGuessReaction() {
        m_ComputerGuess = new GuessPoint(0, 0);
        m_GameStats = new GameStatistics();
    }

}
