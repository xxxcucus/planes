#include "planeround.h"
#include <ctime>

PlaneRound::PlaneRound(int rowNo, int colNo, int planeNo):
	m_rowNo(rowNo), m_colNo(colNo), m_planeNo(planeNo)
{
	//initializes the random number generator
	time_t timer;
	struct tm y2k = { 0 };
	double seconds = 0.0;
	y2k.tm_hour = 0;   y2k.tm_min = 0; y2k.tm_sec = 0;
	y2k.tm_year = 100; y2k.tm_mon = 0; y2k.tm_mday = 1;
	time(&timer);  /* get current time; same as: timer = time(NULL)  */
	seconds = difftime(timer, mktime(&y2k));
	srand(int(floor(seconds)));

	//builds the plane grid objects
	m_PlayerGrid = new PlaneGrid(m_rowNo, m_colNo, m_planeNo, false);
	m_ComputerGrid = new PlaneGrid(m_rowNo, m_colNo, m_planeNo, true);

	//builds the computer logic object
	m_computerLogic = new ComputerLogic(m_rowNo, m_colNo, m_planeNo);

	reset();
	initRound();
} 

PlaneRound::~PlaneRound()
{
	delete m_computerLogic;
	delete m_ComputerGrid;
	delete m_PlayerGrid;
}

//resets the PlaneRound object
void PlaneRound::reset()
{
	m_PlayerGrid->resetGrid();
	m_ComputerGrid->resetGrid();

	m_playerGuessList.clear();
	m_computerGuessList.clear();

	m_gameStats.reset();
	m_computerLogic->reset();
}

void PlaneRound::initRound()
{
	m_PlayerGrid->initGrid();
	m_ComputerGrid->initGrid();
	m_State = GameStages::BoardEditing;
	m_isComputerFirst = !m_isComputerFirst;
	m_playerGuessList.clear();
	m_computerGuessList.clear();

	m_gameStats.reset();
	m_computerLogic->reset();
	//reset();
}

//TODO: do we need to evaluate the player guess here ?
void PlaneRound::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr)
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
		pgr.m_isPlayerWinner = isPlayerWinner;
	} else {
		pgr.m_RoundEnds = false;
	}

	pgr.m_GameStats = m_gameStats;
}

GuessPoint PlaneRound::guessComputerMove()
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


bool PlaneRound::roundEnds(bool& isPlayerWinner)
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
bool PlaneRound::enoughGuesses(PlaneGrid* pg, const std::vector<GuessPoint>& guessList) const
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
void PlaneRound::updateGameStats(const GuessPoint& gp, bool isComputer)
{
	m_gameStats.updateStats(gp, isComputer);
}

void PlaneRound::doneEditing() {
	m_State = GameStages::Game;
}

void PlaneRound::roundEnds() {
	m_State = GameStages::GameNotStarted;
}

/*
	-2 - plane head
	-1 - plane intersection
	0 - is not on plane
	i - plane but not head
*/

int PlaneRound::getPlaneSquareType(int row, int col, bool isComputer)
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

void PlaneRound::rotatePlane(int idx) {
	m_PlayerGrid->rotatePlane(idx);
}

void PlaneRound::movePlaneLeft(int idx) {
	m_PlayerGrid->movePlaneLeft(idx);
}

void PlaneRound::movePlaneRight(int idx) {
	m_PlayerGrid->movePlaneRight(idx);
}

void PlaneRound::movePlaneUpwards(int idx) {
	m_PlayerGrid->movePlaneUpwards(idx);
}

void PlaneRound::movePlaneDownwards(int idx) {
	m_PlayerGrid->movePlaneDownwards(idx);
}