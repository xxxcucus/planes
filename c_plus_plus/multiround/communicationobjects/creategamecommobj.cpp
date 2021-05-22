#include "creategamecommobj.h"

#include <QMessageBox>
#include "viewmodels/gameviewmodel.h"

bool CreateGameCommObj::makeRequest(const QString& gameName)
{
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox(m_ParentWidget);
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

void CreateGameCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    QMessageBox msgBox(m_ParentWidget);
    msgBox.setText("Game creation successfull!"); 
    msgBox.exec();               
    m_GlobalData->m_GameData.m_GameId = retJson.value("id").toString().toLong();
    m_GlobalData->m_GameData.m_GameName = retJson.value("gameName").toString();
    m_GlobalData->m_GameData.m_RoundId = 0;
    m_GlobalData->m_GameData.m_UserId = 0;
    m_GlobalData->m_GameData.m_OtherUserId = 0;
    long int userId2 = retJson.value("secondPlayerId").toString().toLong();
    m_GlobalData->m_UserData.m_UserId = userId2;
    emit gameCreated(m_GameName, m_GlobalData->m_UserData.m_UserName); 
}

bool CreateGameCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName") && reply.contains("currentRoundId")
        && reply.contains("firstPlayerId") && reply.contains("secondPlayerId"));

}
