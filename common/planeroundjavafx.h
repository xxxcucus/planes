#ifndef __PLANE_ROUND_JAVAFX_
#define __PLANE_ROUND_JAVAFX_

#include "planegrid.h"
#include "computerlogic.h"
#include "gamestatistics.h"
#include "guesspoint.h"


struct PlayerGuessReaction {
	//victory for computer or player
	bool m_RoundEnds = false;
	//who won
	bool m_isComputerWinner = false;
	//if no victory then a computer move is generated
	bool m_ComputerMoveGenerated = false;
	//which computer move was generated 
	GuessPoint m_ComputerGuess;
	GameStatistics m_GameStats;

	PlayerGuessReaction() : m_ComputerGuess(0, 0) { }
};

class PlaneRoundJavaFx {
public:
	enum class GameStages { GameNotStarted, BoardEditing, Game };

	//constructs the round object
	PlaneRoundJavaFx(PlaneGrid* playerGrid, PlaneGrid* computerGrid, ComputerLogic* logic, bool isComputerFirst);

	//inits a new round
	void initRound();
	void roundEnds();

	bool didGameEnd() {
		return m_State == GameStages::GameNotStarted;
	}

	void playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr);

	void rotatePlane(int idx);
	void movePlaneLeft(int idx);
	void movePlaneRight(int idx);
	void movePlaneUpwards(int idx);
	void movePlaneDownwards(int idx);

	void doneEditing();

private:
	//update game statistics
	void updateGameStats(const GuessPoint& gp, bool isComputer);
	//tests whether all of the planes have been guessed
	bool enoughGuesses(PlaneGrid* pg, const std::vector<GuessPoint>& guessList) const;
	//based on the available information makes the next move for the computer
	GuessPoint guessComputerMove();
	//resets the round
	void reset();
	//is there a winner
	bool roundEnds(bool& isPlayerWinner);

private:
	//whether the computer or the player moves first
	bool m_isComputerFirst;
	//the  game statistics
	GameStatistics m_gameStats;

	//the player and computer's grid
	PlaneGrid* m_PlayerGrid;
	PlaneGrid* m_ComputerGrid;

	//the list of guesses for computer and player
	std::vector<GuessPoint> m_computerGuessList;
	std::vector<GuessPoint> m_playerGuessList;

	//the computer's strategy
	ComputerLogic* m_computerLogic;

	GameStages m_State = GameStages::GameNotStarted;
};


#endif