#include "requestopponentmovescommobj.h"


#include <QMessageBox>
#include <QJsonArray>

#include "viewmodels/getopponentemovesviewmodel.h"
#include "multiplayerround.h"

bool RequestOpponentMovesCommObj::makeRequest(int opponentMoveIndex)
{
    //TODO: validate the whole global data
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    GetOpponentsMovesViewModel opponentViewModel;
    opponentViewModel.m_GameId = m_GlobalData->m_GameData.m_GameId;
    opponentViewModel.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    opponentViewModel.m_OwnUserId = m_GlobalData->m_GameData.m_UserId;
    opponentViewModel.m_OpponentUserId = m_GlobalData->m_GameData.m_OtherUserId;
    opponentViewModel.m_MoveIndex = opponentMoveIndex;
    
    m_RequestData = opponentViewModel.toJson();
    
    makeRequestBasis(true);
    return true;
}


//the same as savemovecommobj
void RequestOpponentMovesCommObj::finishedRequest()
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
            m_MultiRound->addOpponentMove(gp, moveObject.value("moveIndex").toInt());
            emit opponentMoveGenerated(gp);
        }
    }
}

bool RequestOpponentMovesCommObj::validateReply(const QJsonObject& reply) {
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
