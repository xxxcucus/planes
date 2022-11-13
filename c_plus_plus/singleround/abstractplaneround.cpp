#include "abstractplaneround.h"

#include <ctime>
#include <cmath>


AbstractPlaneRound::AbstractPlaneRound(int rowNo, int colNo, int planeNo):
	m_PlayerGrid(new PlaneGrid(rowNo, colNo, planeNo, false)),
	m_ComputerGrid(new PlaneGrid(rowNo, colNo, planeNo, true)),
	m_rowNo(rowNo), m_colNo(colNo), m_planeNo(planeNo)
{
} 

AbstractPlaneRound::~AbstractPlaneRound()
{
	delete m_ComputerGrid;
	delete m_PlayerGrid;
}

//resets the PlaneRound object
void AbstractPlaneRound::reset()
{
	m_PlayerGrid->resetGrid();
	m_ComputerGrid->resetGrid();

	m_playerGuessList.clear();
	m_computerGuessList.clear();

	m_gameStats.reset();
}

void AbstractPlaneRound::initRound()
{
	m_PlayerGrid->initGrid();
	m_ComputerGrid->initGrid();
	m_State = GameStages::BoardEditing;
	m_isComputerFirst = !m_isComputerFirst;
	m_playerGuessList.clear();
	m_computerGuessList.clear();

	m_gameStats.reset();
	//reset();
}


bool AbstractPlaneRound::roundEnds(bool& isPlayerWinner, bool& isComputerWinner)
{
	isPlayerWinner = enoughGuesses(m_PlayerGrid, m_computerGuessList);
	isComputerWinner = enoughGuesses(m_ComputerGrid, m_playerGuessList);

	return (isPlayerWinner || isComputerWinner);
}

//decides whether all the planes have been guessed
bool AbstractPlaneRound::enoughGuesses(PlaneGrid* pg, const std::vector<GuessPoint>& guessList) const
{
	//for testing draw score
	//if (guessList.size() > 10)
	//	return true;
	int count = 0;

	for (unsigned int i = 0; i < guessList.size(); i++) {
		GuessPoint gp = guessList.at(i);
		if (gp.m_type == GuessPoint::Dead)
			count++;
	}

	return (count >= pg->getPlaneNo());
}

//based on a guesspoint updates the game stats
bool AbstractPlaneRound::updateGameStats(const GuessPoint& gp, bool isComputer)
{
    if ((!isComputer && !m_gameStats.playerFinished(m_planeNo)) || (isComputer && !m_gameStats.computerFinished(m_planeNo))) {
        m_gameStats.updateStats(gp, isComputer);
        return true;
    } else {
        return false;
    }
}

void AbstractPlaneRound::resetGameScore() {
	m_gameStats.resetGameScore();
}

bool AbstractPlaneRound::playerFinished() {
    return m_gameStats.playerFinished(m_planeNo);
}

bool AbstractPlaneRound::computerFinished()
{
    return m_gameStats.computerFinished(m_planeNo);
}


void AbstractPlaneRound::doneEditing() {
	m_State = GameStages::Game;
}

void AbstractPlaneRound::roundEnds() {
	m_State = GameStages::GameNotStarted;
}

/*
	-2 - plane head
	-1 - plane intersection
	0 - is not on plane
	i - plane but not head
*/

int AbstractPlaneRound::getPlaneSquareType(int row, int col, bool isComputer)
{
	bool isOnPlane = false;

	if (isComputer) {
		int idxInPlanePointList = 0;
		isOnPlane = m_ComputerGrid->isPointOnPlane(row, col, idxInPlanePointList);
		if (!isOnPlane)
			return 0;
		int annotation = m_ComputerGrid->getPlanePointAnnotation(idxInPlanePointList);
		std::vector<int> planesIdx = m_ComputerGrid->decodeAnnotation(annotation);
		if (planesIdx.size() > 1) {
			return -1;
		} 
		
		if (planesIdx.size() == 1) {
			if (planesIdx[0] < 0)
				return -2;
			else
				return (planesIdx[0] + 1);
		}
	} else {
		int idxInPlanePointList = 0;
		isOnPlane = m_PlayerGrid->isPointOnPlane(row, col, idxInPlanePointList);
		if (!isOnPlane)
			return 0;
		int annotation = m_PlayerGrid->getPlanePointAnnotation(idxInPlanePointList);
		std::vector<int> planesIdx = m_PlayerGrid->decodeAnnotation(annotation);
		if (planesIdx.size() > 1) {
			return -1;
		}

		if (planesIdx.size() == 1) {
			if (planesIdx[0] < 0)
				return -2;
			else
				return (planesIdx[0] + 1);
		}
	}

	return 0;
}

bool AbstractPlaneRound::rotatePlane(int idx) {
	m_PlayerGrid->rotatePlane(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool AbstractPlaneRound::movePlaneLeft(int idx) {
	m_PlayerGrid->movePlaneLeft(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool AbstractPlaneRound::movePlaneRight(int idx) {
	m_PlayerGrid->movePlaneRight(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool AbstractPlaneRound::movePlaneUpwards(int idx) {
	m_PlayerGrid->movePlaneUpwards(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool AbstractPlaneRound::movePlaneDownwards(int idx) {
	m_PlayerGrid->movePlaneDownwards(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}


void AbstractPlaneRound::updateGameStatsAndGuessListPlayer(const GuessPoint& gp) {
	//update the game statistics
	updateGameStats(gp, false);
	//add the player's guess to the list of guesses
	//assume that the guess is different from the other guesses
	m_playerGuessList.push_back(gp);
	m_ComputerGrid->addGuess(gp);
	//printf("guess is dead %d \n", gp.isDead());
	//printf("show after kill %d \n", m_RoundOptions.m_ShowPlaneAfterKill);
}

/**void AbstractPlaneRound::updateGameStatsAndReactionComputer(PlayerGuessReaction& pgr) {
	GuessPoint gpc = guessComputerMove();
	m_PlayerGrid->addGuess(gpc);
	updateGameStats(gpc, true);
	pgr.m_ComputerMoveGenerated = true;
	pgr.m_ComputerGuess = gpc;
}**/
