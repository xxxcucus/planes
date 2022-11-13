#include "planeround.h"
#include <ctime>
#include <cmath>
#include <algorithm>

PlaneRound::PlaneRound(int rowNo, int colNo, int planeNo):
    AbstractPlaneRound(rowNo, colNo, planeNo),
    m_computerLogic(new ComputerLogic(rowNo, colNo, planeNo))
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

	reset();
	initRound();

} 

PlaneRound::~PlaneRound()
{
	delete m_computerLogic;
}

//resets the PlaneRound object
void PlaneRound::reset()
{
    AbstractPlaneRound::reset();
    m_computerLogic->reset();
}

void PlaneRound::initRound()
{
    AbstractPlaneRound::initRound();
	m_computerLogic->reset();
}


void PlaneRound::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr)
{
	if (m_State != AbstractPlaneRound::GameStages::Game)
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
		m_State = AbstractPlaneRound::GameStages::GameNotStarted;
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


GuessPoint PlaneRound::guessComputerMove()
{
	PlanesCommonTools::Coordinate2D qp;
	//use the computer strategy to get a move
	m_computerLogic->makeChoice(qp, m_RoundOptions.m_ComputerSkillLevel);

	//use the player grid to see the result of the grid
	GuessPoint::Type tp = m_PlayerGrid->getGuessResult(qp);
	GuessPoint gp(qp.x(), qp.y(), tp);

	//add the data to the computer strategy
	m_computerLogic->addData(gp);

	//update the computer guess list
	m_computerGuessList.push_back(gp);

	return gp;
}


//options
bool PlaneRound::setComputerSkill(int computerSkill) {
	if (m_State == AbstractPlaneRound::GameStages::Game)
		return false;

	m_RoundOptions.m_ComputerSkillLevel = computerSkill;
	return true;
}


bool PlaneRound::setShowPlaneAfterKill(bool showPlane) {
	if (m_State == AbstractPlaneRound::GameStages::Game)
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
	m_ComputerGrid->addGuess(gp);
	//printf("guess is dead %d \n", gp.isDead());
	//printf("show after kill %d \n", m_RoundOptions.m_ShowPlaneAfterKill);
	if (gp.isDead() && m_RoundOptions.m_ShowPlaneAfterKill) {
		int pos = m_ComputerGrid->searchPlane(gp.m_row, gp.m_col);
		//printf("pos %d\n", pos);
		if (pos < 0)
			return;
		std::vector<PlanesCommonTools::Coordinate2D> planePoints;
		m_ComputerGrid->getPlanePoints(pos, planePoints);
		for (int i = 0; i < planePoints.size(); i++) {
			GuessPoint gp1(planePoints[i].x(), planePoints[i].y(), GuessPoint::Hit);
			std::vector<GuessPoint>::iterator it = std::find(m_playerGuessList.begin(), m_playerGuessList.end(), gp1);
			if (it == m_playerGuessList.end()) {
				m_playerGuessList.push_back(gp1);
				m_ComputerGrid->addGuess(gp1);
			}
		}
	}
}

void PlaneRound::updateGameStatsAndReactionComputer(PlayerGuessReaction& pgr) {
	GuessPoint gpc = guessComputerMove();
	m_PlayerGrid->addGuess(gpc);
	updateGameStats(gpc, true);
	pgr.m_ComputerMoveGenerated = true;
	pgr.m_ComputerGuess = gpc;
}
