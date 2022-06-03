#include "sendmovecommobj.h"

#include <QMessageBox>
#include <QJsonArray>

#include "viewmodels/unsentmovesviewmodel.h"
#include "multiplayerround.h"



bool SendMoveCommObj::makeRequest(const std::vector<GuessPoint>& guessList, const std::vector<int>& notSentMoves, const std::vector<int>& receivedMoves, bool fromFinishedSlot) 
{
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox(m_ParentWidget);
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    UnsentMovesViewModel newMoveData;
    newMoveData.m_GameId = m_GlobalData->m_GameData.m_GameId;
    newMoveData.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    newMoveData.m_OwnUserId = m_GlobalData->m_GameData.m_UserId;
    newMoveData.m_OpponentUserId = m_GlobalData->m_GameData.m_OtherUserId;
    
    int maxReceivedIndex = 0;
    std::vector<int> notReceivedMoves = computeNotReceivedMoves(receivedMoves, maxReceivedIndex);
    
    newMoveData.m_OpponentMoveIndex = maxReceivedIndex;
    newMoveData.m_NotReceivedMoveIndex = notReceivedMoves;

    std::vector<SingleMoveViewModel> moves;
    for (auto idx : notSentMoves) {
        SingleMoveViewModel move;
        GuessPoint gp = guessList[idx - 1];
        move.m_MoveX = gp.m_row;
        move.m_MoveY = gp.m_col;
        move.m_MoveIndex = idx;
        moves.push_back(move);
    }
    
    newMoveData.m_NotSentMovesIndex = moves;
    m_RequestData = newMoveData.toJson();
    
    /*qDebug() << "Not sent moves ";
    for (auto idx: notSentMoves)
        qDebug() << idx;
    qDebug() << "Received moves ";
    for (auto idx: receivedMoves)
        qDebug() << idx;*/
    
    if (makeRequestBasis(true, fromFinishedSlot)) {
        m_LastNotSentMoveIndexSucces = notSentMoves;
        m_LastNotSentMoveIndexError.clear();
    } else {
        m_LastNotSentMoveIndexError = notSentMoves;
    }
    
    return true;
}

void SendMoveCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    bool rCancelled = retJson.value("cancelled").toBool();
    
    if (rCancelled) {
        m_MultiRound->setRoundCancelled();
        emit roundCancelled();
        return;
    }
    
    for (auto idx : m_LastNotSentMoveIndexSucces)
    m_MultiRound->deleteFromNotSentList(idx);
    
    QJsonValue movesObject = retJson.value("listMoves");
    QJsonArray movesArray = movesObject.toArray();
    
    for (int i = 0; i < movesArray.size(); i++) {
        QJsonValue moveValue = movesArray.at(i);
        QJsonObject moveObject = moveValue.toObject();
        if (moveObject.contains("moveX") && moveObject.contains("moveY")) {
            GuessPoint gp = GuessPoint(moveObject.value("moveX").toInt(), moveObject.value("moveY").toInt());
            int moveIndex = moveObject.value("moveIndex").toInt();
            if (!m_MultiRound->moveAlreadyReceived(moveIndex)) {
                //qDebug() << "add opponent move to grid ";
                m_MultiRound->addOpponentMove(gp, moveIndex);
                emit opponentMoveGenerated(gp); 
            }
        }
    }
    
    //TODO: when not sent elements with error exist and player finished  send them as well
    //TODO: this here only when the user has guessed everything ??
    if (!m_LastNotSentMoveIndexError.empty()) {
        emit allGuessedAndMovesStillToSend();
    } else {
        emit allMovesSent();
    }
}

bool SendMoveCommObj::validateReply(const QJsonObject& reply) {
    if (!(reply.contains("roundId") && reply.contains("opponentUserId") && reply.contains("startIndex") && reply.contains("cancelled") && reply.contains("listMoves"))) {
        //qDebug() << "error 1";
        return false;
    }

    if (!checkLong(reply.value("roundId").toString()))
        return false;

    if (!checkLong(reply.value("opponentUserId").toString()))
        return false;
    
    QJsonValue movesObject = reply.value("listMoves");
    if (!movesObject.isArray()) {
        //qDebug() << "error 2";
        return false;
    }
    
    QJsonArray movesArray = movesObject.toArray();
    if (movesArray.size() > 100) {
        //qDebug() << "error 3 " << movesArray.size();
        return false;
    }
    
    for (int i = 0; i < movesArray.size(); i++) {
        QJsonValue moveValue = movesArray.at(i);
        if (!moveValue.isObject()) {
            //qDebug() << "error 4 " << i;
            return false;
        }
            
        QJsonObject moveObject = moveValue.toObject();
        if (!(moveObject.contains("moveX") && moveObject.contains("moveY")))
            return false;
        
        if (!(checkInt(moveObject.value("moveX"))))
            return false;
        
        if (!(checkInt(moveObject.value("moveY"))))
            return false;
        
    }
    
    return true;
}

std::vector<int> SendMoveCommObj::computeNotReceivedMoves(const std::vector<int>& receivedMoves, int& maxReceivedIndex) {
    if(receivedMoves.empty()) {
        maxReceivedIndex = 0;
        return std::vector<int>();
    }
            
    auto it = std::max_element(receivedMoves.begin(), receivedMoves.end());
    maxReceivedIndex = *it;
    
    std::vector<int> retVal;
    for (int i = 0; i < maxReceivedIndex; i++) {
        auto it1 = std::find(receivedMoves.begin(), receivedMoves.end(), i);
        if (it1 == receivedMoves.end())
            retVal.push_back(i);
    }
    return retVal;
}
