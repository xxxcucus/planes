#ifndef GAMESTATISTICS_H
#define GAMESTATISTICS_H

#include "guesspoint.h"

//keeps track of the moves, hits, dead, and misses of both the player and the computer
struct GameStatistics
{
    //keeps the number of moves and various guesses in the current round
    int m_playerMoves = 0;
    int m_playerHits = 0;
    int m_playerDead = 0;
    int m_playerMisses = 0;
    int m_computerMoves = 0;
    int m_computerHits = 0;
    int m_computerDead = 0;
    int m_computerMisses = 0;
    //keeps the score
    int m_playerWins = 0;
    int m_computerWins = 0;
	int m_draws = 0;

    //constructor
    GameStatistics();
    //resets the fields related to one round of the game
    void reset();
    //updates the statistics for one round with one guess
    void updateStats(const GuessPoint& gp, bool isComputer);
    //adds to the score
    void updateWins(bool isComputerWinner);
	void updateDraws();
    
    bool playerFinished(int noPlanes) {
        return m_playerDead >= noPlanes;
    }
    
    bool computerFinished(int noPlanes) {
        return m_computerDead >= noPlanes;
    }
};

#endif // GAMESTATISTICS_H
