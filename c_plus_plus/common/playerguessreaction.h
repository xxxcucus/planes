#ifndef __PLAYER_GUESS_REACTION__
#define __PLAYER_GUESS_REACTION__

#include "guesspoint.h"
#include "gamestatistics.h"

struct PlayerGuessReaction {
	//victory for computer or player
	bool m_RoundEnds = false;
	//who won
	bool m_isPlayerWinner = false;
	//is draw
	bool m_isDraw = false;
	//if no victory then a computer move is generated
	bool m_ComputerMoveGenerated = false;
	//which computer move was generated 
	GuessPoint m_ComputerGuess;
	GameStatistics m_GameStats;

	PlayerGuessReaction() : m_ComputerGuess(0, 0) { }
};


#endif
