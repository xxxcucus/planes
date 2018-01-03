#ifndef GAMESTATISTICS_H
#define GAMESTATISTICS_H

#include "guesspoint.h"

//keeps track of the moves, hits, dead, and misses of both the player and the computer
struct GameStatistics
{
    //keeps the number of moves and various guesses in the current round
    int m_playerMoves;
    int m_playerHits;
    int m_playerDead;
    int m_playerMisses;
    int m_computerMoves;
    int m_computerHits;
    int m_computerDead;
    int m_computerMisses;
    //keeps the score
    int m_playerWins;
    int m_computerWins;

    //constructor
    GameStatistics();
    //resets the fields related to one round of the game
    void reset();
    //updates the statistics for one round with one guess
    void updateStats(const GuessPoint& gp, bool isComputer);
    //adds to the score
    void updateWins(bool isComputerWinner);
};

#endif // GAMESTATISTICS_H
