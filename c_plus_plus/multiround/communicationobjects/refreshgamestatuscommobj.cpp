#include "refreshgamestatuscommobj.h"


#include <QMessageBox>
#include "viewmodels/gameviewmodel.h"

bool RefreshGameStatusCommObj::makeRequest(const QString& gameName)
{
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox(m_ParentWidget);
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    m_GameName = gameName;
    m_GlobalData->m_GameData.reset(); //TODO: is this correct
        
    GameViewModel gameData;
    gameData.m_GameName = gameName; 
    gameData.m_Username = m_GlobalData->m_UserData.m_UserName;
    gameData.m_UserId = 0; 
    gameData.m_GameId = 0;
    
    m_RequestData = gameData.toJson();
    
    makeRequestBasis(true);
    return true;
}

void RefreshGameStatusCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    m_GlobalData->m_GameData.m_GameId = retJson.value("id").toString().toLong();
    m_GlobalData->m_GameData.m_RoundId = retJson.value("currentRoundId").toString().toLong();
    
    QString gameName = retJson.value("gameName").toString();
    QString firstPlayerName = retJson.value("firstPlayerName").toString();
    QString secondPlayerName = retJson.value("secondPlayerName").toString();
    QString currentRoundId = retJson.value("currentRoundId").toString();
    
    m_GlobalData->m_GameData.m_GameId = retJson.value("id").toString().toLong(); 
    m_GlobalData->m_GameData.m_GameName = retJson.value("gameName").toString();
    m_GlobalData->m_GameData.m_RoundId = retJson.value("currentRoundId").toString().toLong();
    m_GlobalData->m_GameData.m_UserId = m_GlobalData->m_UserData.m_UserId;
    long int userId1 = retJson.value("firstPlayerId").toString().toLong();
    long int userId2 = retJson.value("secondPlayerId").toString().toLong();
    //qDebug() << "userId1 " << userId1;
    //qDebug() << "userId2 " << userId2;
    //qDebug() << "m_GlobalData.m_UserData.m_UserId " << m_GlobalData->m_UserData.m_UserId;
    m_GlobalData->m_GameData.m_OtherUserId = (userId1 == m_GlobalData->m_UserData.m_UserId) ? userId2 : userId1; //TODO validation m_UserData.m_UserId should be sent from server
    m_GlobalData->m_GameData.m_OtherUsername = (userId1 == m_GlobalData->m_UserData.m_UserId) ? secondPlayerName : firstPlayerName;
    
    bool exists = retJson.value("exists").toBool();
    
    emit refreshStatus(exists, gameName, firstPlayerName, secondPlayerName, currentRoundId);
}

bool RefreshGameStatusCommObj::validateReply(const QJsonObject& reply) {
    if (!(reply.contains("exists") && reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName") && reply.contains("currentRoundId") && reply.contains("firstPlayerId") && reply.contains("secondPlayerId")))
        return false;
    
    if (!checkLong(reply.value("id").toString()))
        return false;

    if (!checkLong(reply.value("currentRoundId").toString()))
        return false;

    if (!checkLong(reply.value("firstPlayerId").toString()))
        return false;

    if (!checkLong(reply.value("secondPlayerId").toString()))
        return false;
        
    return true;
    
    
}
