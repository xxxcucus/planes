#include "connecttogamecommobj.h"

#include <QMessageBox>
#include "viewmodels/gameviewmodel.h"

bool ConnectToGameCommObj::makeRequest(const QString& gameName)
{
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    m_GameName = gameName;
    m_GlobalData->m_GameData.reset();
        
    GameViewModel gameData;
    gameData.m_GameName = gameName; 
    gameData.m_Username = m_GlobalData->m_UserData.m_UserName; 
    gameData.m_UserId = 0; 
    gameData.m_GameId = 0;
    
    m_RequestData = gameData.toJson();
    
    makeRequestBasis(true);
    return true;
}

void ConnectToGameCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    QMessageBox msgBox;
    msgBox.setText("Connection to game successfull!"); 
    msgBox.exec();               
    
    m_GlobalData->m_GameData.m_GameId = retJson.value("id").toString().toLong(); //TODO conversion errors
    m_GlobalData->m_GameData.m_RoundId = retJson.value("currentRoundId").toString().toLong();
    long int userId1 = retJson.value("firstPlayerId").toString().toLong();
    long int userId2 = retJson.value("secondPlayerId").toString().toLong();
    m_GlobalData->m_GameData.m_OtherUserId =  userId1; //so does the server
    m_GlobalData->m_GameData.m_UserId = userId2;
    m_GlobalData->m_UserData.m_UserId = userId2;
    QString firstPlayerName = retJson.value("firstPlayerName").toString();
    QString currentRoundId = retJson.value("currentRoundId").toString(); 
    emit gameConnectedTo(m_GameName, firstPlayerName, m_GlobalData->m_UserData.m_UserName, currentRoundId);
}

bool ConnectToGameCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName") && reply.contains("currentRoundId")
        && reply.contains("firstPlayerId") && reply.contains("secondPlayerId"));
}
