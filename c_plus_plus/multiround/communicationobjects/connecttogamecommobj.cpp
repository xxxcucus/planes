#include "connecttogamecommobj.h"

#include <QTimer>
#include "viewmodels/gameviewmodel.h"


bool ConnectToGameCommObj::makeRequest(const QString& gameName)
{
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }

    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        emit logMessage("No user logged in!");
        return false;
    }

    m_GameName = gameName;
    m_GlobalData->m_GameData.reset();
    m_RequestData = prepareViewModel(m_GameName).toJson();
    
    makeRequestBasis(true);
    return true;
}

GameViewModel ConnectToGameCommObj::prepareViewModel(const QString& gameName) {
    GameViewModel gameData;
    gameData.m_GameName = gameName;
    gameData.m_Username = m_GlobalData->m_UserData.m_UserName;
    gameData.m_UserId = 0;
    gameData.m_GameId = 0;
    return gameData;
}

void ConnectToGameCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    emit logMessage("User connected to game!");
    processResponse(retJson);
}

void ConnectToGameCommObj::processResponse(const QJsonObject& retJson) {
    QString receivedGameName = retJson.value("gameName").toString();
    bool resetGameScore = false;
    if (receivedGameName != m_GlobalData->m_GameData.m_GameName)
        resetGameScore = true;

    m_GlobalData->m_GameData.m_GameId = retJson.value("id").toString().toLong();
    m_GlobalData->m_GameData.m_GameName = retJson.value("gameName").toString();
    m_GlobalData->m_GameData.m_RoundId = retJson.value("currentRoundId").toString().toLong();
    long int userId1 = retJson.value("firstPlayerId").toString().toLong();
    long int userId2 = retJson.value("secondPlayerId").toString().toLong();
    m_GlobalData->m_GameData.m_OtherUserId = userId1; //so does the server
    m_GlobalData->m_GameData.m_OtherUsername = retJson.value("firstPlayerName").toString();
    m_GlobalData->m_GameData.m_UserId = userId2;
    m_GlobalData->m_UserData.m_UserId = userId2;
    QString firstPlayerName = retJson.value("firstPlayerName").toString();
    QString currentRoundId = retJson.value("currentRoundId").toString();

    emit gameConnectedTo(m_GameName, firstPlayerName, m_GlobalData->m_UserData.m_UserName, currentRoundId, resetGameScore);
}

bool ConnectToGameCommObj::validateReply(const QJsonObject& reply) {
    if (!(reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName") && reply.contains("currentRoundId")
        && reply.contains("firstPlayerId") && reply.contains("secondPlayerId"))) 
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
