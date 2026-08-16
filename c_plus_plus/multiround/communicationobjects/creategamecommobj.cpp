#include "creategamecommobj.h"

#include <QTimer>
#include "viewmodels/gameviewmodel.h"


bool CreateGameCommObj::makeRequest(const QString& gameName) {
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

GameViewModel CreateGameCommObj::prepareViewModel(const QString& gameName) {
    GameViewModel gameData;
    gameData.m_GameName = gameName;
    gameData.m_Username = m_GlobalData->m_UserData.m_UserName;
    gameData.m_UserId = 0;
    gameData.m_GameId = 0;
    return gameData;
}

void CreateGameCommObj::finishedRequest() {
    QJsonObject retJson;
    if (!finishRequestHelper(retJson))
        return;

    emit logMessage("Game created!");
    processResponse(retJson);
}

void CreateGameCommObj::processResponse(const QJsonObject& retJson) {
    QString receivedGameName = retJson.value("gameName").toString();
    bool resetGameScore = false;
    if (receivedGameName != m_GlobalData->m_GameData.m_GameName)
        resetGameScore = true;
    m_GlobalData->m_GameData.m_GameId = retJson.value("id").toString().toLong();
    m_GlobalData->m_GameData.m_GameName = retJson.value("gameName").toString();
    m_GlobalData->m_GameData.m_RoundId = 0;
    m_GlobalData->m_GameData.m_UserId = 0;
    m_GlobalData->m_GameData.m_OtherUserId = 0;
    long int userId2 = retJson.value("secondPlayerId").toString().toLong();
    m_GlobalData->m_UserData.m_UserId = userId2;
    emit gameCreated(m_GameName, m_GlobalData->m_UserData.m_UserName, resetGameScore);
}

bool CreateGameCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName") && reply.contains("currentRoundId")
        && reply.contains("firstPlayerId") && reply.contains("secondPlayerId"));

}
