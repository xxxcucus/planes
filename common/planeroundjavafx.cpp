#include "planeroundjavafx.h"

PlaneRoundJavaFx::PlaneRoundJavaFx(PlaneGrid* playerGrid, PlaneGrid* computerGrid, ComputerLogic* logic, bool isComputerFirst): 
	m_isComputerFirst(isComputerFirst),
	m_PlayerGrid(playerGrid),
	m_ComputerGrid(computerGrid),
	m_computerLogic(logic)
{
	reset();
	initRound();
} 


//resets the PlaneRound object
void PlaneRoundJavaFx::reset()
{
	m_PlayerGrid->resetGrid();
	m_ComputerGrid->resetGrid();

	m_playerGuessList.clear();
	m_computerGuessList.clear();

	m_gameStats.reset();
	m_computerLogic->reset();
}

void PlaneRoundJavaFx::initRound()
{
	m_PlayerGrid->initGrid();
	m_ComputerGrid->initGrid();
	m_State = GameStages::BoardEditing;
	m_isComputerFirst = !m_isComputerFirst;
	//reset();
}

//TODO: do we need to evaluate the player guess here ?
void PlaneRoundJavaFx::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr)
{
	if (m_State != GameStages::Game)
		return;

	if (m_isComputerFirst) {
		GuessPoint gpc = guessComputerMove();
		updateGameStats(gpc, true);
		pgr.m_ComputerMoveGenerated = true;
		pgr.m_ComputerGuess = gpc;

		//update the game statistics
		updateGameStats(gp, false);
		//add the player's guess to the list of guesses
		//assume that the guess is different from the other guesses
		m_playerGuessList.push_back(gp);
	} else {
		//update the game statistics
		updateGameStats(gp, false);
		//add the player's guess to the list of guesses
		//assume that the guess is different from the other guesses
		m_playerGuessList.push_back(gp);

		GuessPoint gpc = guessComputerMove();
		updateGameStats(gpc, true);
		pgr.m_ComputerMoveGenerated = true;
		pgr.m_ComputerGuess = gpc;
	}

	bool isPlayerWinner = false;
	if (roundEnds(isPlayerWinner)) {
		m_gameStats.updateWins(isPlayerWinner);
		pgr.m_RoundEnds = true;
		m_State = GameStages::GameNotStarted;
	} else {
		pgr.m_RoundEnds = false;
	}

	pgr.m_GameStats = m_gameStats;
}

void PlaneRoundJavaFx::rotatePlane(int idx) {
	if (m_State != GameStages::BoardEditing)
		return;
	m_PlayerGrid->rotatePlane(idx);
}

void PlaneRoundJavaFx::movePlaneLeft(int idx) {
	if (m_State != GameStages::BoardEditing)
		return;
	m_PlayerGrid->movePlaneLeft(idx);
}

void PlaneRoundJavaFx::movePlaneRight(int idx) {
	m_PlayerGrid->movePlaneRight(idx);
}

void PlaneRoundJavaFx::movePlaneUpwards(int idx) {
	if (m_State != GameStages::BoardEditing)
		return;
	m_PlayerGrid->movePlaneUpwards(idx);
}

void PlaneRoundJavaFx::movePlaneDownwards(int idx) {
	if (m_State != GameStages::BoardEditing)
		return;
	m_PlayerGrid->movePlaneDownwards(idx);
}

GuessPoint PlaneRoundJavaFx::guessComputerMove()
{
	PlanesCommonTools::Coordinate2D qp;
	//use the computer strategy to get a move
	m_computerLogic->makeChoice(qp);

	//use the player grid to see the result of the grid
	GuessPoint::Type tp = m_PlayerGrid->getGuessResult(qp);
	GuessPoint gp(qp.x(), qp.y(), tp);

	//add the data to the computer strategy
	m_computerLogic->addData(gp);

	//update the computer guess list
	m_computerGuessList.push_back(gp);

	return gp;
}


bool PlaneRoundJavaFx::roundEnds(bool& isPlayerWinner)
{
	//at equal scores computer wins
	isPlayerWinner = false;

	bool playerFinished = enoughGuesses(m_PlayerGrid, m_computerGuessList);
	bool computerFinished = enoughGuesses(m_ComputerGrid, m_playerGuessList);

	if (!computerFinished && playerFinished)
		isPlayerWinner = true;

	return (playerFinished || computerFinished);
}

//decides whether all the planes have been guessed
bool PlaneRoundJavaFx::enoughGuesses(PlaneGrid* pg, const std::vector<GuessPoint>& guessList) const
{
	int count = 0;

	for (unsigned int i = 0; i < guessList.size(); i++) {
		GuessPoint gp = guessList.at(i);
		if (gp.m_type == GuessPoint::Dead)
			count++;
	}

	return (count >= pg->getPlaneNo());
}

//based on a guesspoint updates the game stats
void PlaneRoundJavaFx::updateGameStats(const GuessPoint& gp, bool isComputer)
{
	m_gameStats.updateStats(gp, isComputer);
}

void PlaneRoundJavaFx::doneEditing() {
	m_State = GameStages::Game;
}

void PlaneRoundJavaFx::roundEnds() {
	m_State = GameStages::GameNotStarted;
}