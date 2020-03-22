package com.planes.common;


public class GameStatistics {
    //constructor
    public GameStatistics() {
        reset();
        m_computerWins = 0;
        m_playerWins = 0;
    }
    //resets the fields related to one round of the game
    public void reset() {
        m_playerMoves = 0;
        m_playerHits = 0;
        m_playerDead = 0;
        m_playerMisses = 0;
        m_computerMoves = 0;
        m_computerHits = 0;
        m_computerDead = 0;
        m_computerMisses = 0;
    }
    //updates the statistics for one round with one guess
    public void updateStats(final GuessPoint gp, boolean isComputer) {
        if (isComputer) {
            m_computerMoves++;
            if(gp.m_type == Type.Dead)
                m_computerDead++;
            if(gp.m_type == Type.Hit)
                m_computerHits++;
            if(gp.m_type == Type.Miss)
                m_computerMisses++;
        } else {
            m_playerMoves++;
            if(gp.m_type == Type.Dead)
                m_playerDead++;
            if(gp.m_type == Type.Hit)
                m_playerHits++;
            if(gp.m_type == Type.Miss)
                m_playerMisses++;
        }
    }
    //adds to the score
    public void updateWins(boolean isComputerWinner) {
        if(isComputerWinner)
            m_computerWins++;
        else
            m_playerWins++;
    }


    //keeps the number of moves and various guesses in the current round
    public int m_playerMoves = 0;
    public int m_playerHits = 0;
    public int m_playerDead = 0;
    public int m_playerMisses = 0;
    public int m_computerMoves = 0;
    public int m_computerHits = 0;
    public int m_computerDead = 0;
    public int m_computerMisses = 0;
    //keeps the score
    public int m_playerWins = 0;
    public int m_computerWins = 0;
}
