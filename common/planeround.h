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
	bool m_isPlayerWinner = false;
	//if no victory then a computer move is generated
	bool m_ComputerMoveGenerated = false;
	//which computer move was generated 
	GuessPoint m_ComputerGuess;
	GameStatistics m_GameStats;

	PlayerGuessReaction() : m_ComputerGuess(0, 0) { }
};

class PlaneRound {
public:
	enum class GameStages { GameNotStarted, BoardEditing, Game };

	//constructs the round object
	//PlaneRound(PlaneGrid* playerGrid, PlaneGrid* computerGrid, ComputerLogic* logic, bool isComputerFirst);
	PlaneRound(int rowNo, int colNo, int planeNo);
	~PlaneRound();

	//inits a new round
	void initRound();
	void roundEnds();

	bool didGameEnd() {
		return m_State == GameStages::GameNotStarted;
	}

	void playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr);

	//TODO
	/*void rotatePlane(int idx);
	void movePlaneLeft(int idx);
	void movePlaneRight(int idx);
	void movePlaneUpwards(int idx);
	void movePlaneDownwards(int idx);*/

	void doneEditing();

	PlaneGrid* playerGrid() { return m_PlayerGrid; }
	PlaneGrid* computerGrid() { return m_ComputerGrid; }
	ComputerLogic* computerLogic() { return m_computerLogic; }

	int getRowNo() const {
		return m_rowNo;
	}

	int getColNo() const {
		return m_colNo;
	}

	int getPlaneNo() const {
		return m_planeNo;
	}

	int getPlaneSquareType(int i, int j, bool isComputer);

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
	bool m_isComputerFirst = false;
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

	//size of the grid and number of planes
	int m_rowNo = 10;
	int m_colNo = 10;
	int m_planeNo = 3;
};


#endif