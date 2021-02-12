#include "multiplayerround.h"

#include <QTextCodec>
#include <QMessageBox>
#include <QJsonArray>
#include "communicationtools.h"
#include "viewmodels/newmoveviewmodel.h"
#include "viewmodels/getopponentemovesviewmodel.h"

MultiplayerRound::MultiplayerRound(int rows, int cols, int planeNo, QNetworkAccessManager* networkManager, GlobalData* globalData, QSettings* settings)
    : AbstractPlaneRound(rows, cols, planeNo), m_NetworkManager(networkManager), m_GlobalData(globalData), m_Settings(settings)
{
    m_IsSinglePlayer = false;
    m_CreateGameObj = new CreateGameCommObj("/game/create/", "creating game", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData);
    connect(m_CreateGameObj, &CreateGameCommObj::gameCreated, this, &MultiplayerRound::gameCreated);
    m_ConnectToGameObj = new ConnectToGameCommObj("/game/connect/", "connecting to game ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData);
    connect(m_ConnectToGameObj, &ConnectToGameCommObj::gameConnectedTo, this, &MultiplayerRound::gameConnectedTo);
    m_RefreshGameStatusCommObj = new RefreshGameStatusCommObj("/game/status/", "refreshing game status ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData);
    connect(m_RefreshGameStatusCommObj, &RefreshGameStatusCommObj::refreshStatus, this, &MultiplayerRound::refreshStatus);
    m_LoginCommObj = new LoginCommObj("/login/", "logging in ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData);
    connect(m_LoginCommObj, &LoginCommObj::loginCompleted, this, &MultiplayerRound::loginCompleted);
    m_RegisterCommObj = new RegisterCommObj("/users/registration_request", "registering ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData);
    connect(m_RegisterCommObj, &RegisterCommObj::noRobotRegistration, this, &MultiplayerRound::noRobotRegistration);
    m_NoRobotCommObj = new NoRobotCommObj("/users/registration_confirm", "registering ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData);
    connect(m_NoRobotCommObj, &NoRobotCommObj::registrationComplete, this, &MultiplayerRound::registrationComplete);
    connect(m_NoRobotCommObj, &NoRobotCommObj::registrationFailed, this, &MultiplayerRound::registrationFailed);
    m_SendPlanePositionsCommObj = new SendPlanePositionsCommObj("/round/myplanespositions", "sending plane positions ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData, this);
    connect(m_SendPlanePositionsCommObj, &SendPlanePositionsCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);
    connect(m_SendPlanePositionsCommObj, &SendPlanePositionsCommObj::opponentPlanePositionsReceived, this, &MultiplayerRound::opponentPlanePositionsReceived);    
    connect(m_SendPlanePositionsCommObj, &SendPlanePositionsCommObj::waitForOpponentPlanePositions, this, &MultiplayerRound::waitForOpponentPlanePositions);    
    m_AcquireOpponentPlanePositions = new AcquireOpponentPositionsCommObj("/round/otherplanespositions", "acquiring plane positions ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData, this);
    connect(m_AcquireOpponentPlanePositions, &AcquireOpponentPositionsCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);
    connect(m_AcquireOpponentPlanePositions, &AcquireOpponentPositionsCommObj::opponentPlanePositionsReceived, this, &MultiplayerRound::opponentPlanePositionsReceived);    
    m_SendMoveCommObj= new SendMoveCommObj("/round/mymove", "sending move ", m_NetworkManager, m_Settings, m_IsSinglePlayer, m_GlobalData, this);
    connect(m_SendMoveCommObj, &SendMoveCommObj::opponentMoveGenerated, this, &MultiplayerRound::opponentMoveGenerated);
    connect(m_SendMoveCommObj, &SendMoveCommObj::roundCancelled, this, &MultiplayerRound::roundWasCancelled);
    
    reset();
    initRound();
}

//resets the PlaneRound object
void MultiplayerRound::reset()
{
    AbstractPlaneRound::reset();
    m_PlayerMoveIndex = 0;
    m_ComputerMoveIndex = 0;
    m_GlobalData->m_GameData.reset();
    m_GlobalData->m_UserData.reset();
}

void MultiplayerRound::initRound()
{
    AbstractPlaneRound::initRound();
    m_PlayerMoveIndex = 0;
    m_ComputerMoveIndex = 0;
    //round id is set through separate function
}

void MultiplayerRound::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) {
    if (m_State != AbstractPlaneRound::GameStages::Game) {
        QMessageBox msgBox;
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
        m_PlayerMoveIndex++; //TODO do we need this
        
        sendMove(gp, m_PlayerMoveIndex, m_ComputerMoveIndex);
        
    } else {
        qDebug() << "Update game statistics returned false";
        //TODO: round ending logic
    }
    
    
    //evaluate the guess using computergrid and save in playerguessreaction
    //send the guess together with the guess index to the server
    //slot for answer should confirm sending and receive opponents moves
}

void MultiplayerRound::playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) {
}

//TODO to add function in plane round as well and in AbstractPlaneRound
void MultiplayerRound::getPlayerPlaneNo(int pos, Plane& pl) {
    m_PlayerGrid->getPlane(pos, pl);
}

bool MultiplayerRound::setComputerPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient) {
    m_State = AbstractPlaneRound::GameStages::Game;
    return m_ComputerGrid->initGridByUser(plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient, plane3_x, plane3_y, plane3_orient);
}

void MultiplayerRound::requestOpponentMoves()
{
    GetOpponentsMovesViewModel opponentViewModel;
    opponentViewModel.m_GameId = m_GlobalData->m_GameData.m_GameId;
    opponentViewModel.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    opponentViewModel.m_OwnUserId = m_GlobalData->m_GameData.m_UserId;
    opponentViewModel.m_OpponentUserId = m_GlobalData->m_GameData.m_OtherUserId;
    opponentViewModel.m_MoveIndex = m_ComputerMoveIndex;
    
    if (m_OpponentMoveReply != nullptr)
        delete m_OpponentMoveReply;

    m_OpponentMoveReply = CommunicationTools::buildPostRequestWithAuth("/round/othermoves", m_Settings->value("multiplayer/serverpath").toString(), opponentViewModel.toJson(), m_GlobalData->m_UserData.m_AuthToken, m_NetworkManager);

    connect(m_OpponentMoveReply, &QNetworkReply::finished, this, &MultiplayerRound::finishedAcquireOpponentMoves);
    connect(m_OpponentMoveReply, &QNetworkReply::errorOccurred, this, &MultiplayerRound::errorAcquireOpponentMoves);
}

//TODO it is the same function as with reply for my move
void MultiplayerRound::finishedAcquireOpponentMoves() {
    if (m_OpponentMoveReply->error() != QNetworkReply::NoError) {
        return;
    }
    
    QByteArray reply = m_OpponentMoveReply->readAll();
    QString newMoveClickedReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject newMoveClickedReplyJson = CommunicationTools::objectFromString(newMoveClickedReplyQString);
    
    qDebug() << newMoveClickedReplyQString;
    
    if (!validateOpponentMovesReply(newMoveClickedReplyJson)) {
        QMessageBox msgBox;
        msgBox.setText("Opponent moves reply not recognized"); 
        msgBox.exec();

        return;
    }

    bool rCancelled = newMoveClickedReplyJson.value("cancelled").toBool();
    
    if (rCancelled) {
        setRoundCancelled();
        emit roundWasCancelled();
        return;
    }

    
    QJsonValue movesObject = newMoveClickedReplyJson.value("listMoves");
    QJsonArray movesArray = movesObject.toArray();
    
    for (int i = 0; i < movesArray.size(); i++) {
        QJsonValue moveValue = movesArray.at(i);
        QJsonObject moveObject = moveValue.toObject();
        if (moveObject.contains("moveX") && moveObject.contains("moveY")) {
            GuessPoint gp = GuessPoint(moveObject.value("moveX").toInt(), moveObject.value("moveY").toInt());
            if (updateGameStats(gp, true)) {
                GuessPoint::Type guessResult =  m_PlayerGrid->getGuessResult(PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col));
                gp.setType(guessResult);
                m_computerGuessList.push_back(gp);
                m_PlayerGrid->addGuess(gp);
                m_ComputerMoveIndex++;
                emit opponentMoveGenerated(gp);
            } else {
                qDebug() << "update game stats computer returned false";
                //TODO: round ending logic 
            }
        }
    }    
}


void MultiplayerRound::errorAcquireOpponentMoves(QNetworkReply::NetworkError code) {
    CommunicationTools::treatCommunicationError("sending move to server ", m_PlayerMoveReply);        
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
    long int roundId = currentRoundId.toLong(); //TODO error treatment
    startNewRound(roundId);
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

void MultiplayerRound::noRobotRegister(const QString& requestId, const QString& answer)
{
    m_NoRobotCommObj->makeRequest(requestId, answer);
}

void MultiplayerRound::sendPlanePositions() {
    m_SendPlanePositionsCommObj->makeRequest();
}

void MultiplayerRound::acquireOpponentPlanePositions()
{
    m_AcquireOpponentPlanePositions->makeRequest();
}

void MultiplayerRound::sendMove(const GuessPoint& gp, int ownMoveIndex, int opponentMoveIndex) {
    m_SendMoveCommObj->makeRequest(gp, ownMoveIndex, opponentMoveIndex);
}

void MultiplayerRound::addOpponentMove(GuessPoint& gp)
{
    GuessPoint::Type guessResult =  m_PlayerGrid->getGuessResult(PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col));
    gp.setType(guessResult);
    m_computerGuessList.push_back(gp);
    m_PlayerGrid->addGuess(gp);
    m_ComputerMoveIndex++;
}
