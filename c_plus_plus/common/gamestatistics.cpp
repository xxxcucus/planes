#include "gamestatistics.h"

//constructor
GameStatistics::GameStatistics()
{
    reset();
    m_computerWins = 0;
    m_playerWins = 0;
	m_draws = 0;
}

//resets the fields related to one round
void GameStatistics::reset()
{
    m_playerMoves = 0;
    m_playerHits = 0;
    m_playerDead = 0;
    m_playerMisses = 0;
    m_computerMoves = 0;
    m_computerHits = 0;
    m_computerDead = 0;
    m_computerMisses = 0;
}

//updates the statistical data for one round with a new GuessPoint
void GameStatistics::updateStats(const GuessPoint& gp, bool isComputer)
{
    if (isComputer) {
        m_computerMoves++;
        if(gp.m_type == GuessPoint::Dead)
            m_computerDead++;
        if(gp.m_type == GuessPoint::Hit)
            m_computerHits++;
        if(gp.m_type == GuessPoint::Miss)
            m_computerMisses++;
    } else {
        m_playerMoves++;
        if(gp.m_type == GuessPoint::Dead)
            m_playerDead++;
        if(gp.m_type == GuessPoint::Hit)
            m_playerHits++;
        if(gp.m_type == GuessPoint::Miss)
            m_playerMisses++;
    }
}

//updates the score
void GameStatistics::updateWins(bool isComputerWinner)
{
    if(isComputerWinner)
        m_computerWins++;
    else
        m_playerWins++;
}

void GameStatistics::updateDraws()
{
	m_draws++;
}
