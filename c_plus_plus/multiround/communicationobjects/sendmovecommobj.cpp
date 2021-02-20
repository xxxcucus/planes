#include "sendmovecommobj.h"

#include <QMessageBox>
#include <QJsonArray>

#include "viewmodels/newmoveviewmodel.h"
#include "multiplayerround.h"


//TODO: send moves with index 
bool SendMoveCommObj::makeRequest(const GuessPoint& gp, int ownMoveIndex, int opponentMoveIndex)
{
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    NewMoveViewModel newMoveData;
    newMoveData.m_GameId = m_GlobalData->m_GameData.m_GameId;
    newMoveData.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    newMoveData.m_OwnUserId = m_GlobalData->m_GameData.m_UserId;
    newMoveData.m_OpponentUserId = m_GlobalData->m_GameData.m_OtherUserId;
    newMoveData.m_OwnMoveIndex = ownMoveIndex;
    newMoveData.m_OpponentMoveIndex = opponentMoveIndex;
    newMoveData.m_MoveX = gp.m_row;
    newMoveData.m_MoveY = gp.m_col;
    
    m_RequestData = newMoveData.toJson();
    
    makeRequestBasis(true);
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
    
    QJsonValue movesObject = retJson.value("listMoves");
    QJsonArray movesArray = movesObject.toArray();
    
    for (int i = 0; i < movesArray.size(); i++) {
        QJsonValue moveValue = movesArray.at(i);
        QJsonObject moveObject = moveValue.toObject();
        if (moveObject.contains("moveX") && moveObject.contains("moveY")) {
            GuessPoint gp = GuessPoint(moveObject.value("moveX").toInt(), moveObject.value("moveY").toInt());
            qDebug() << "add opponent move to grid ";
            m_MultiRound->addOpponentMove(gp);
            emit opponentMoveGenerated(gp);
        }
    }
}

bool SendMoveCommObj::validateReply(const QJsonObject& reply) {
    if (!(reply.contains("roundId") && reply.contains("opponentUserId") && reply.contains("startIndex") && reply.contains("cancelled") && reply.contains("listMoves"))) {
        qDebug() << "error 1";
        return false;
    }
    
    //TODO: validation round ids, user id and start index
    
    QJsonValue movesObject = reply.value("listMoves");
    if (!movesObject.isArray()) {
        qDebug() << "error 2";
        return false;
    }
    
    QJsonArray movesArray = movesObject.toArray();
    if (movesArray.size() > 100) {
        qDebug() << "error 3 " << movesArray.size();
        return false;
    }
    
    for (int i = 0; i < movesArray.size(); i++) {
        QJsonValue moveValue = movesArray.at(i);
        if (!moveValue.isObject()) {
            qDebug() << "error 4 " << i;
            return false;
        }
            
        QJsonObject moveObject = moveValue.toObject();
        if (!(moveObject.contains("moveX") && moveObject.contains("moveY")))
            return false;
    }
    
    return true;
}
