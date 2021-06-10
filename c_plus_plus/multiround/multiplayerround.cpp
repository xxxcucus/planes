#include "multiplayerround.h"

#include <QTextCodec>
#include <QMessageBox>
#include <QJsonArray>
#include "communicationtools.h"
#include "viewmodels/newmoveviewmodel.h"
#include "viewmodels/getopponentemovesviewmodel.h"

MultiplayerRound::MultiplayerRound(int rows, int cols, int planeNo, QWidget* parentWidget, QNetworkAccessManager* networkManager, GlobalData* globalData, QSettings* settings, GameInfo* gameInfo)
    : AbstractPlaneRound(rows, cols, planeNo), m_ParentWidget(parentWidget), m_NetworkManager(networkManager), m_GlobalData(globalData), m_Settings(settings), m_GameInfo(gameInfo)
{
    m_CreateGameObj = new CreateGameCommObj("/game/create/", "creating game", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
    connect(m_CreateGameObj, &CreateGameCommObj::gameCreated, this, &MultiplayerRound::gameCreatedSlot);
    m_ConnectToGameObj = new ConnectToGameCommObj("/game/connect/", "connecting to game ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
    connect(m_ConnectToGameObj, &ConnectToGameCommObj::gameConnectedTo, this, &MultiplayerRound::connectedToGameSlot);
    m_RefreshGameStatusCommObj = new RefreshGameStatusCommObj("/game/status/", "refreshing game status ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
    connect(m_RefreshGameStatusCommObj, &RefreshGameStatusCommObj::refreshStatus, this, &MultiplayerRound::refreshStatus);
    m_LoginCommObj = new LoginCommObj("/login/", "logging in ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
    connect(m_LoginCommObj, &LoginCommObj::loginCompleted, this, &MultiplayerRound::loginCompleted);
    connect(m_LoginCommObj, &LoginCommObj::loginFailed, this, &MultiplayerRound::loginFailed);
    m_RegisterCommObj = new RegisterCommObj("/users/registration_request", "registering ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
    connect(m_RegisterCommObj, &RegisterCommObj::noRobotRegistration, this, &MultiplayerRound::noRobotRegistration);
    m_NoRobotCommObj = new NoRobotCommObj("/users/registration_confirm", "registering ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
    connect(m_NoRobotCommObj, &NoRobotCommObj::registrationComplete, this, &MultiplayerRound::registrationComplete);
    connect(m_NoRobotCommObj, &NoRobotCommObj::registrationFailed, this, &MultiplayerRound::registrationFailed);
    m_SendPlanePositionsCommObj = new SendPlanePositionsCommObj("/round/myplanespositions", "sending plane positions ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData, this);
    connect(m_SendPlanePositionsCommObj, &SendPlanePositionsCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);
    connect(m_SendPlanePositionsCommObj, &SendPlanePositionsCommObj::opponentPlanePositionsReceived, this, &MultiplayerRound::opponentPlanePositionsReceived);    
    connect(m_SendPlanePositionsCommObj, &SendPlanePositionsCommObj::waitForOpponentPlanePositions, this, &MultiplayerRound::waitForOpponentPlanePositions);    
    m_AcquireOpponentPlanePositions = new AcquireOpponentPositionsCommObj("/round/otherplanespositions", "acquiring plane positions ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData, this);
    connect(m_AcquireOpponentPlanePositions, &AcquireOpponentPositionsCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);
    connect(m_AcquireOpponentPlanePositions, &AcquireOpponentPositionsCommObj::opponentPlanePositionsReceived, this, &MultiplayerRound::opponentPlanePositionsReceived);    
    m_SendMoveCommObj = new SendMoveCommObj("/round/mymove", "sending move ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData, this);
    connect(m_SendMoveCommObj, &SendMoveCommObj::opponentMoveGenerated, this, &MultiplayerRound::opponentMoveGenerated);
    connect(m_SendMoveCommObj, &SendMoveCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);
    connect(m_SendMoveCommObj, &SendMoveCommObj::allGuessedAndMovesStillToSend, this, &MultiplayerRound::sendLastMoves);
    connect(m_SendMoveCommObj, &SendMoveCommObj::allMovesSent, this, &MultiplayerRound::allMovesSentSlot);
    /*m_RequestOpponentMovesObj = new RequestOpponentMovesCommObj("/round/othermoves", "requesting moves ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData, this);
    connect(m_RequestOpponentMovesObj, &RequestOpponentMovesCommObj::opponentMoveGenerated, this, &MultiplayerRound::opponentMoveGenerated);
    connect(m_RequestOpponentMovesObj, &RequestOpponentMovesCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);*/
    m_CancelRoundCommObj = new CancelRoundCommObj("/round/cancel", "cancelling round ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData, this);
    connect(m_CancelRoundCommObj, &CancelRoundCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);
    m_StartNewRoundCommObj = new StartNewRoundCommObj("/round/start", "starting round ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData, this);
    connect(m_StartNewRoundCommObj, &StartNewRoundCommObj::startNewRound, this, &MultiplayerRound::newRoundStarted);
    m_SendWinnerCommObj = new SendWinnerCommObj("/round/end", "ending round ", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
    m_GetServerVersionCommObj = new GetServerVersionCommObj("/status/getversion", "getting version", m_ParentWidget, m_NetworkManager, m_Settings, m_GameInfo->getSinglePlayer(), m_GlobalData);
        
    reset();
    initRound();
}

//resets the PlaneRound object
void MultiplayerRound::reset()
{
    AbstractPlaneRound::reset();
    m_PlayerMoveIndex = 0;
    m_ComputerMoveIndex = 0;
    m_WinnerFound = false;
    m_GlobalData->m_GameData.reset();
    m_GlobalData->m_UserData.reset();
}

void MultiplayerRound::initRound()
{
    AbstractPlaneRound::initRound();
    m_WinnerFound = false;
    m_PlayerMoveIndex = 0;
    m_ComputerMoveIndex = 0;
    m_NotSentMoves.clear();
    m_ReceivedMoves.clear();
 
    emit gameStatsUpdated(m_gameStats);
    //round id is set through separate function
}

void MultiplayerRound::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) {
    if (m_State != AbstractPlaneRound::GameStages::Game) {
        QMessageBox msgBox(m_ParentWidget);
        msgBox.setText("Not ready to play game.\n You do not have the opponent's planes positions!"); 
        msgBox.exec();

        return;        
    }
            
    //update the game statistics
	if (updateGameStats(gp, false)) {
        //add the player's guess to the list of guesses
        //assume that the guess is different from the other guesses
        m_playerGuessList.push_back(gp);
        m_ComputerGrid->addGuess(gp);

        //GuessPoint::Type guessResult =  m_ComputerGrid->getGuessResult(PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col));
        m_PlayerMoveIndex++; 
        sendMove(gp, m_PlayerMoveIndex);
    } else {
        //qDebug() << "Player has already found all planes";
    }
    
    bool draw = false;
    long int winnerId = 0;
    bool isPlayerWinner = false;
    
    if (!m_WinnerFound) {
        if (checkRoundEnd(draw, winnerId, isPlayerWinner)) {
            emit winnerSent(isPlayerWinner, draw);
            sendWinner(draw, winnerId);
        }
        emit gameStatsUpdated(m_gameStats);
    }
}

void MultiplayerRound::sendLastMoves() {
    //qDebug() << "sendLastMoves";
    if (playerFinished() && !m_NotSentMoves.empty())
        m_SendMoveCommObj->makeRequest(m_playerGuessList, m_NotSentMoves, m_ReceivedMoves);    
}


void MultiplayerRound::playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) {
}

//TODO to add function in plane round as well as in AbstractPlaneRound
void MultiplayerRound::getPlayerPlaneNo(int pos, Plane& pl) {
    m_PlayerGrid->getPlane(pos, pl);
}

bool MultiplayerRound::setComputerPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient) {
    m_State = AbstractPlaneRound::GameStages::Game;
    return m_ComputerGrid->initGridByUser(plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient, plane3_x, plane3_y, plane3_orient);
}

void MultiplayerRound::setRoundCancelled()
{
    m_State = AbstractPlaneRound::GameStages::GameNotStarted;
}

void MultiplayerRound::startNewRound(long int desiredRoundId) {
    //TODO cancel current round
    if (desiredRoundId == m_GlobalData->m_GameData.m_RoundId)
        return;
    initRound();
    m_GlobalData->m_GameData.m_RoundId = desiredRoundId;
}

void MultiplayerRound::createGame(const QString& gameName)
{
    m_CreateGameObj->makeRequest(gameName);
}

void MultiplayerRound::connectToGame(const QString& gameName)
{
    m_ConnectToGameObj->makeRequest(gameName);
}

void MultiplayerRound::connectedToGameSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId) {
    bool ok = false;
    long int roundId = currentRoundId.toLong(&ok, 10);
    if (!ok) {
        //qDebug() << "RoundId is not Long !!";
        return;
    }
    startNewRound(roundId);
    emit gameConnectedTo(gameName, firstPlayerName, secondPlayerName, currentRoundId);
}

void MultiplayerRound::gameCreatedSlot(const QString& gameName, const QString& userName) {
    initRound();
    emit gameCreated(gameName, userName);
}

void MultiplayerRound::refreshGameStatus(const QString& gameName)
{
    m_RefreshGameStatusCommObj->makeRequest(gameName);
}

void MultiplayerRound::login(const QString& username, const QString& password)
{
    m_LoginCommObj->makeRequest(username, password);
}

void MultiplayerRound::registerUser(const QString& username, const QString& password)
{
    m_RegisterCommObj->makeRequest(username, password);
}

void MultiplayerRound::noRobotRegister(const QString& requestId, const QString& answer) {
    m_NoRobotCommObj->makeRequest(requestId, answer);
}

void MultiplayerRound::sendPlanePositions() {
    m_SendPlanePositionsCommObj->makeRequest();
}

void MultiplayerRound::acquireOpponentPlanePositions()
{
    m_AcquireOpponentPlanePositions->makeRequest();
}

void MultiplayerRound::sendMove(const GuessPoint& gp, int ownMoveIndex) {
    addToNotSentList(ownMoveIndex);
    m_SendMoveCommObj->makeRequest(m_playerGuessList, m_NotSentMoves, m_ReceivedMoves);
}

bool MultiplayerRound::checkRoundEnd(bool& draw, long int& winnerId, bool& isPlayerWinner) {


    isPlayerWinner = false;

    if (m_gameStats.computerFinished(m_planeNo) && m_gameStats.playerFinished(m_planeNo)) {
        if (m_ComputerMoveIndex > m_PlayerMoveIndex) {
            //player winner 
            m_gameStats.updateWins(false);
            winnerId = m_GlobalData->m_GameData.m_UserId;
            isPlayerWinner = true;
            m_WinnerFound = true;
            return true;
        } else if (m_ComputerMoveIndex < m_PlayerMoveIndex) {
            //computer winner
            m_gameStats.updateWins(true);
            winnerId = m_GlobalData->m_GameData.m_OtherUserId;
            m_WinnerFound = true;
            return true;
        } else {
            //draw
            draw = true;
            m_gameStats.updateDraws();
            m_WinnerFound = true;
            return true;
        }            
    } 
    
    
    if (m_gameStats.computerFinished(m_planeNo) && !m_gameStats.playerFinished(m_planeNo)) {
        //qDebug() << "Computer finished and player not finished " << m_ComputerMoveIndex << " " << m_PlayerMoveIndex;
        if (m_ComputerMoveIndex <= m_PlayerMoveIndex) {
            //computer winner
            m_gameStats.updateWins(true);
            winnerId = m_GlobalData->m_GameData.m_OtherUserId;
            m_WinnerFound = true;
            return true;
        } 
    }

    if (!m_gameStats.computerFinished(m_planeNo) && m_gameStats.playerFinished(m_planeNo)) {
        //qDebug() << "Computer not finished and player finished " << m_ComputerMoveIndex << " " << m_PlayerMoveIndex;
        if (m_ComputerMoveIndex >= m_PlayerMoveIndex) {
            //computer winner
            m_gameStats.updateWins(false);
            winnerId = m_GlobalData->m_GameData.m_UserId;
            isPlayerWinner = true;
            m_WinnerFound = true;
            return true;
        } 
    }
    
    return false;
}

void MultiplayerRound::addOpponentMove(GuessPoint& gp, int moveIndex)
{
    addToReceivedList(moveIndex);
    GuessPoint::Type guessResult =  m_PlayerGrid->getGuessResult(PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col));
    gp.setType(guessResult);    
    
    if (updateGameStats(gp, true)) {
        m_computerGuessList.push_back(gp);
        m_PlayerGrid->addGuess(gp);
        m_ComputerMoveIndex++;
    } else {
        //qDebug() << "computer has already found all planes";
    }

    bool draw = false;
    long int winnerId = 0;
    bool isPlayerWinner = false;
    
    if (!m_WinnerFound) {
        if (checkRoundEnd(draw, winnerId, isPlayerWinner)) {
            emit winnerSent(isPlayerWinner, draw);
            sendWinner(draw, winnerId);
        }

        emit gameStatsUpdated(m_gameStats);
    }
}

void MultiplayerRound::requestOpponentMoves()
{
    m_SendMoveCommObj->makeRequest(m_playerGuessList, m_NotSentMoves, m_ReceivedMoves);
}

void MultiplayerRound::cancelRound()
{
    m_CancelRoundCommObj->makeRequest();
}

void MultiplayerRound::startNewRound()
{
    m_StartNewRoundCommObj->makeRequest();
}

void MultiplayerRound::sendWinner(bool draw, long winnerId)
{
    m_SendWinnerCommObj->makeRequest(draw, winnerId);
}

void MultiplayerRound::deleteFromNotSentList(int value)
{
    std::vector<int>::iterator it = std::find(m_NotSentMoves.begin(), m_NotSentMoves.end(), value);
    if (it != m_NotSentMoves.end())
        m_NotSentMoves.erase(it);
}

void MultiplayerRound::addToNotSentList(int value)
{
    std::vector<int>::iterator it = std::find(m_NotSentMoves.begin(), m_NotSentMoves.end(), value);
    if (it == m_NotSentMoves.end())
        m_NotSentMoves.push_back(value);

}

bool MultiplayerRound::moveAlreadyReceived(int moveIndex)
{
    std::vector<int>::iterator it = std::find(m_ReceivedMoves.begin(), m_ReceivedMoves.end(), moveIndex);
    return (it != m_ReceivedMoves.end());
}

void MultiplayerRound::addToReceivedList(int value)
{
    m_ReceivedMoves.push_back(value);
}

void MultiplayerRound::testServerVersion()
{
    m_GetServerVersionCommObj->makeRequest();
}

void MultiplayerRound::allMovesSentSlot()
{
    //qDebug() << "allMovesSent" ;
    if (playerFinished() && !computerFinished())
        emit allMovesSent();
}
