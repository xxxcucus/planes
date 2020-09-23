#include "planeround.h"
#include <ctime>
#include <cmath>

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


void PlaneRound::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr)
{
	if (m_State != GameStages::Game)
		return;

	if (m_isComputerFirst) {
		//TODO: to synchronize list of guesses in PlaneRound, PlaneGrid and ComputerBoard, PlayerBoard

		updateGameStatsAndReactionComputer(pgr);
		updateGameStatsAndGuessListPlayer(gp);
	} else {
		updateGameStatsAndGuessListPlayer(gp);
		updateGameStatsAndReactionComputer(pgr);
	}

	bool isPlayerWinner = false;
	bool isComputerWinner = false;
	if (roundEnds(isPlayerWinner, isComputerWinner)) {
		if (isPlayerWinner && isComputerWinner) {
			m_gameStats.updateDraws();
			pgr.m_isDraw = true;
		} else {
			m_gameStats.updateWins(isPlayerWinner);
			pgr.m_isDraw = false;
		}
		
		pgr.m_RoundEnds = true;
		m_State = GameStages::GameNotStarted;
		pgr.m_isPlayerWinner = isPlayerWinner;
	} else {
		pgr.m_RoundEnds = false;
	}

	pgr.m_GameStats = m_gameStats;
}

void PlaneRound::playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr)
{
	PlanesCommonTools::Coordinate2D qp(col, row);
	guessRes = m_ComputerGrid->getGuessResult(qp);
	GuessPoint gp(qp.x(), qp.y(), guessRes);

	playerGuess(gp, pgr);
}

int PlaneRound::playerGuessAlreadyMade(int row, int col) {
	PlanesCommonTools::Coordinate2D qp(col, row);
	for (auto guess : m_playerGuessList) {
		if (guess.m_row == col && guess.m_col == row)
			return 1;
	}
	return 0;
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

bool PlaneRound::roundEnds(bool& isPlayerWinner, bool& isComputerWinner)
{
	isPlayerWinner = enoughGuesses(m_PlayerGrid, m_computerGuessList);
	isComputerWinner = enoughGuesses(m_ComputerGrid, m_playerGuessList);

	return (isPlayerWinner || isComputerWinner);
}

//decides whether all the planes have been guessed
bool PlaneRound::enoughGuesses(PlaneGrid* pg, const std::vector<GuessPoint>& guessList) const
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

bool PlaneRound::rotatePlane(int idx) {
	m_PlayerGrid->rotatePlane(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool PlaneRound::movePlaneLeft(int idx) {
	m_PlayerGrid->movePlaneLeft(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool PlaneRound::movePlaneRight(int idx) {
	m_PlayerGrid->movePlaneRight(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool PlaneRound::movePlaneUpwards(int idx) {
	m_PlayerGrid->movePlaneUpwards(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

bool PlaneRound::movePlaneDownwards(int idx) {
	m_PlayerGrid->movePlaneDownwards(idx);
	return !(m_PlayerGrid->doPlanesOverlap() || m_PlayerGrid->isPlaneOutsideGrid());
}

//options
bool PlaneRound::setComputerSkill(int computerSkill) {
	if (m_State == GameStages::Game)
		return false;

	m_RoundOptions.m_ComputerSkillLevel = computerSkill;
	return true;
}


bool PlaneRound::setShowPlaneAfterKill(bool showPlane) {
	if (m_State == GameStages::Game)
		return false;
	m_RoundOptions.m_ShowPlaneAfterKill = showPlane;
	return true;
}

void PlaneRound::updateGameStatsAndGuessListPlayer(const GuessPoint& gp) {
	//update the game statistics
	updateGameStats(gp, false);
	//add the player's guess to the list of guesses
	//assume that the guess is different from the other guesses
	m_playerGuessList.push_back(gp);
	printf("guess is dead %d \n", gp.isDead());
	printf("show after kill %d \n", m_RoundOptions.m_ShowPlaneAfterKill);
	if (gp.isDead() && m_RoundOptions.m_ShowPlaneAfterKill) {
		int pos = m_ComputerGrid->searchPlane(gp.m_row, gp.m_col);
		printf("pos %d\n", pos);
		if (pos < 0)
			return;
		std::vector<PlanesCommonTools::Coordinate2D> planePoints;
		m_ComputerGrid->getPlanePoints(pos, planePoints);
		for (int i = 0; i < planePoints.size(); i++) {
			GuessPoint gp1(planePoints[i].x(), planePoints[i].y(), GuessPoint::Hit);
			std::vector<GuessPoint>::iterator it = std::find(m_playerGuessList.begin(), m_playerGuessList.end(), gp1);
			if (it == m_playerGuessList.end())
				m_playerGuessList.push_back(gp1);
		}
	}
}

void PlaneRound::updateGameStatsAndReactionComputer(PlayerGuessReaction& pgr) {
	GuessPoint gpc = guessComputerMove();
	updateGameStats(gpc, true);
	pgr.m_ComputerMoveGenerated = true;
	pgr.m_ComputerGuess = gpc;
}